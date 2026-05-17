package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;



@Controller
@RequestMapping("/admin/corridas")
public class CorridaController {
 

   @Autowired
   private CorridaService corridaService;

    

    @GetMapping("")
    public String exibirCorridas(Model model){
        model.addAttribute("corridas", corridaService.todasCorridas());
        return "admin/lista_corridas";
    }


    @GetMapping("/nova")
    public String exibirForm(Model model)  {
        model.addAttribute("corrida",new Corrida());
        return "admin/form";
    }

    @PostMapping("/nova")
    public String formCorrida(Corrida corrida, RedirectAttributes redirect){
        corridaService.saveCorrida(corrida);
        redirect.addAttribute("corrida",corrida);
        redirect.addFlashAttribute("mensagem","Usuario criada com sucesso");
        return "redirect:/admin/corridas";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormDaCorrida(@PathVariable Long id,Model model) {
        Corrida corridaEncontrada = corridaService.findById(id);
        model.addAttribute("corrida",corridaEncontrada);
        return "admin/form";

    }
    

    @PostMapping("/{id}/editar")
    public String atualizarCorrida(@PathVariable Long id, Corrida corrida, RedirectAttributes redirect )  {
        corridaService.atualizarCorrida(id,corrida);
        redirect.addAttribute("id",id);
        redirect.addFlashAttribute("mensagem","Corrida editada com sucesso");
        
        return "redirect:/admin/corridas";
    }

    @PostMapping("/{id}/deletar")
    public String excluirCorrida(@PathVariable Long id,RedirectAttributes redirect){
        corridaService.deletarCorrida(id);
        redirect.addFlashAttribute("mensagem","Corrida excluida com sucesso");

        return "redirect:/admin/corridas";
    }
  
    
    
}