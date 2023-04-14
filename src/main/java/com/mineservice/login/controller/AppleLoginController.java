package com.mineservice.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class AppleLoginController {

    private static final String TEAM_ID = "XQ24HP64B3";
    private static final String REDIRECT_URL = "https://mine.directory/login/oauth_apple";
    private static final String CLIENT_ID = "com.bside.Mine.services";
    private static final String KEY_ID = "5UUZ4LKXZF";
    private static final String AUTH_URL = "https://appleid.apple.com";
    private static final String KEY_PATH = "static/apple/AuthKey_5UUZ4LKXZF.p8";

    @GetMapping("/login/getAppleAuthUrl")
    public @ResponseBody String getAppleAuthUrl(HttpServletRequest request) {
        String reqUrl = AUTH_URL + "/auth/authorize?client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + REDIRECT_URL
            + "&response_type=code id_token&scope=name email&response_mode=form_post";
        return reqUrl;
    }

    @GetMapping("/login/oauth_apple")
    public String oauthApple(HttpServletRequest request,
        @RequestParam(value = "code", required = false) String code,
        HttpServletResponse response,
        Model model) throws NoSuchAlgorithmException, JsonProcessingException {
        if(code == null){
            return "index.html";
        }

        String clientId = CLIENT_ID;
        String clientSecret = createClientSecret(TEAM_ID, CLIENT_ID, KEY_ID, KEY_PATH, AUTH_URL);

        String reqUrl = AUTH_URL + "/auth/token";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("code", code);
        tokenRequest.put("grant_type", "authorization_code");

        String apiResponse = doPost(reqUrl, tokenRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject tokenResponse = objectMapper.readValue(apiResponse, JSONObject.class);

        if(tokenResponse.get("error") == null){
            JSONObject payload = decodeFromIdToken(tokenResponse.getAsString("id_token"));
            String appleUniqueNo = payload.getAsString("sub");
            return appleUniqueNo;

        }else{
            throw new UsernameNotFoundException("사용자 정보가 없습니다.");
        }
    }

    public String doPost(String url, Map<String, String> param) {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        Integer statusCode = null;
        String reasonPhrase = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Map.Entry<String, String>> entrySet = param.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();
                nvps.add(new BasicNameValuePair(fieldName, fieldValue));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            reasonPhrase = response.getStatusLine().getReasonPhrase();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

            if (statusCode != 200) {
                log.error("[error] : " + result);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String createClientSecret(String teamId, String clientId,
        String keyId, String keyPath, String authUrl) throws NoSuchAlgorithmException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        Date now = new Date();

        claimsSet.setIssuer(teamId);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
        claimsSet.setAudience(authUrl);
        claimsSet.setSubject(clientId);

        SignedJWT jwt = new SignedJWT(header, claimsSet);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readPrivateKey(keyPath));
        KeyFactory kf = KeyFactory.getInstance("EC");
        try{
            ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());

            jwt.sign(jwsSigner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jwt.serialize();
    }

    private byte[] readPrivateKey(String keyPath) {

        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try (FileReader keyReader = new FileReader(resource.getFile());
            PemReader pemReader = new PemReader(keyReader)) {
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public JSONObject decodeFromIdToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

            if (payload != null) {
                return payload;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
