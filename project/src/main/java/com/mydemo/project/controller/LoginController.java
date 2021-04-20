package com.mydemo.project.controller;



import com.mydemo.project.dto.LoginDTO;
import com.mydemo.project.service.AccountService;
import com.mydemo.project.service.ResourceService;
import com.mydemo.project.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;

/**
 * @author allen
 */
@Controller
@RequestMapping("auth")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResourceService resourceService;
    /**
     * 用户登陆
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("login")
    public String login(String username , String password, HttpSession session ,
                        RedirectAttributes attributes, Model model){
        //调用service的login方法，得到loginDTO
        LoginDTO loginDTO = accountService.login(username, password);
        //取出错误信息
        String error = loginDTO.getError();
        //根据错误信息进行判断
        if(null == error){
            //错误信息为空，成功登陆
            session.setAttribute("account",loginDTO.getAccount());
            List<ResourceVO> resourceVOS = resourceService
                    .listResourceByRoleId(loginDTO.getAccount().getRoleId());
            model.addAttribute("resources",resourceVOS);

            //将资源转换为代码模块名称的集合
            HashSet<String> module = resourceService.convert(resourceVOS);
            session.setAttribute("module",module);
        }else{
            //获取错误信息
            attributes.addFlashAttribute("error",error);
        }
        return loginDTO.getPath();
    }


    /**
     * 退出方法
     * @param session 记录
     * @return 跳转的页面
     */
    @GetMapping("logout")
    public String logout(HttpSession session){
        //销毁session
        session.invalidate();
        //返回登陆界面
        return "redirect:/";

    }
}
