package com.honji.order.controller;


import com.honji.order.entity.PersonArea;
import com.honji.order.mapper.PersonAreaMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.Person;
import com.honji.order.model.SmallArea;
import com.honji.order.service.IPersonAreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-08-30
 */
@Slf4j
@Controller
@RequestMapping("/person-area")
public class PersonAreaController {
    

    @Autowired
    private IPersonAreaService personAreaService;

    @Autowired
    private PersonAreaMapper personAreaMapper;

    @GetMapping("/index")
    public String index(Model model) {
        List<Person> persons = personAreaMapper.selectPersons();
        List<SmallArea> areas = personAreaMapper.selectAreas();
        model.addAttribute("persons", persons);
        model.addAttribute("areas", areas);
        return "person-area";
    }

    @GetMapping("/get")
    @ResponseBody
    public PersonArea get(@RequestParam String id) {

        return personAreaService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam(required = false) String personName, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(personAreaService.listForIndex(personName, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public int save(@ModelAttribute PersonArea personArea) {
        if (StringUtils.isEmpty(personArea.getId())) {
            return personAreaMapper.save(personArea);
        } else {
            return personAreaMapper.update(personArea);
        }

    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return personAreaService.removeById(id);
    }
    
}
