package com.khureen.greenReview.controller

import com.khureen.greenReview.service.ChecklistElement
import com.khureen.greenReview.service.ChecklistService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController // @Controller를 포함함
class ChecklistController {

    @GetMapping("/checklists")
    fun getChecklist() : List<ChecklistElement>{
        return ChecklistService.all
    }
}