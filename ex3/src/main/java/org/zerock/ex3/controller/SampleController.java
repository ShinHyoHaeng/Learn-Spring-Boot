package org.zerock.ex3.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.ex3.dto.SampleDTO;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1...");
    }

    /*
    @GetMapping({"ex2"})
    public void exModel(Model model){

        // 하나만 출력하는 코드
        *//*
        SampleDTO dto = SampleDTO.builder()
                .sno(1L)
                .first("First...1")
                .last("Last...1")
                .regTime(LocalDateTime.now())
                .build();

        model.addAttribute("dtoTest",dto);
        model.addAttribute("strTest","문자열");
        *//*

        // 교재에 있는 대로 쓴 코드(20개 리스트 만드는 코드)
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First..."+i)
                    .last("Last..."+i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }
    */

    // 링크 처리
    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {
        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First..." + i)
                    .last("Last..." + i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    // inline 속성
    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes) {
        log.info("exIncline...");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First... 100")
                .last("Last... 100")
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("ex3");
    }

    // 레이아웃 템플릿 만들기
    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "exSidebar"})
    public void exLayout() {
        log.info("exLayout...");
    }

}