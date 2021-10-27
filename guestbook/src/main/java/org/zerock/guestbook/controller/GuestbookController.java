package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 annotation
public class GuestbookController {

    private final GuestbookService service; // final로 선언

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list..."+pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    // 기본 등록 화면 출력
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    // 처리 후에 목록 페이지로 이동하도록 설계(original code)
//    @PostMapping("/register")
//    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
//        log.info("dto..."+dto);
//
//        // 새로 추가된 엔티티의 번호
//        Long gno = service.register(dto);
//
//        // 한 번만 화면에서 'msg'라는 이름의 변수를 사용할 수 있도록 처리
//        // msg의 역할: 화면 상에 모달 창을 보여주는 용도(글이 등록된 후에 자동으로 출력)
//        redirectAttributes.addFlashAttribute("msg", gno);
//        return "redirect:/guestbook/list";
//    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto){
        log.info("dto..."+dto);

        // 새로 추가된 엔티티의 번호
        Long gno = service.register(dto);
        return "redirect:/guestbook/list";
    }

    // GET방식으로 gno 값을 받아서 Model에 GuestbookDTO 객체를 담아서 전달하는 코드
    // 나중에 다시 목록 페이지로 돌아가는 데이터를 같이 저장하기 위해 PageRequestDTO를 파라미터로 같이 사용
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno : " + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }
    
    // 삭제 기능(Original code)
//    @PostMapping("/remove")
//    public String remove(long gno, RedirectAttributes redirectAttributes){
//        service.remove(gno);
//        redirectAttributes.addFlashAttribute("msg", gno);
//        return "redirect:/guestbook/list";
//    }

    @PostMapping("/remove")
    public String remove(long gno){
        service.remove(gno);
        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        service.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());
        return "redirect:/guestbook/read";
    }
}