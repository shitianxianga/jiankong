package com.wenlian.jiankong.controller;

import com.wenlian.jiankong.task.SaticScheduleTask;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/task")
public class taskController {
    @PostMapping("/setUrls")
    public String setUrls(@RequestBody String[] urls)
    {
        try {
            SaticScheduleTask.urls=urls;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return  "success";
    }
    @PostMapping("/reloadList")
    public String reloadList()
    {
        try {
            SaticScheduleTask.list=new ArrayList<>( Arrays.asList(SaticScheduleTask.urls));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return  "success";
    }

}
