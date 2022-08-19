package com.qgstudio.controller;

import com.qgstudio.po.Algorithm;
import com.qgstudio.service.AlgorithmService;
import com.qgstudio.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@RestController
@RequestMapping(value = "/api/algorithm", produces = "application/json;charset=UTF-8")
public class AlgorithmController {
    @Autowired
    private AlgorithmService algorithmService;

    @GetMapping
    public Result getAll(){
        List<Algorithm> list = algorithmService.getAll();
        return new Result(list);
    }
}
