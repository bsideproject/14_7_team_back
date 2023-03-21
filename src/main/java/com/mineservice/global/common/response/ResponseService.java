package com.mineservice.global.common.response;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public <T> SingleResponse<T> getSingleResponse(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setData(data);
        setSuccessResponse(response);
        return response;
    }

    public <T> ListResponse<T> getListResponse(List<T> list) {
        ListResponse<T> response = new ListResponse<>();
        response.setList(list);
        setSuccessResponse(response);
        return response;
    }

    public <T> PageResponse<T> getPageResponse(Page<T> list) {
        PageResponse<T> response = new PageResponse<>();
        response.setList(list);
        setSuccessResponse(response);
        return response;
    }

    public CommonResponse getSuccessResponse() {
        CommonResponse response = new CommonResponse();
        setSuccessResponse(response);
        return response;
    }

    private void setSuccessResponse(CommonResponse response) {
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage("success");
    }


}
