package ru.snakesnake.spring.springboot.spring_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.snakesnake.spring.springboot.spring_project.model.Role;
import ru.snakesnake.spring.springboot.spring_project.model.User;
import ru.snakesnake.spring.springboot.spring_project.service.RoleServiceImpl;
import ru.snakesnake.spring.springboot.spring_project.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public String homePage() {
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String commonAdminPageGet(Model model, Principal principal) {
        List<User> listUsers = userService.getAllUsers();
        List<Role> listRoles = roleService.getAllRoles();
        model.addAttribute("allUsers", listUsers);
        model.addAttribute("autUser", userService.getUserByEmail(principal.getName()));
        model.addAttribute("allRoles",listRoles);
        return "bootstrap/admin";
    }

    @PostMapping("/edit")
    public String commonPageEditAddPost(@ModelAttribute("editUser") @Valid User user,
                                        @RequestParam(value = "newRole") String[] role) {
            user.setRoles(getAddRole(role));
            userService.saveUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String commonPageDeletePost(@ModelAttribute("deleteUser") User user) {
        userService.deleteUserById(user.getId());
        return "redirect:/admin";
    }

    private Set<Role> getAddRole(String [] role) {
        Set<Role> roleSet = new HashSet<>();
        for (int i = 0; i < role.length; i++) {
            roleSet.add(roleService.getRoleByName(role[i]));
        }
        return roleSet;
    }

    @GetMapping("/user")
    public String commonUserPageGet(Model model, Principal principal) {
        model.addAttribute("autUser", userService.getUserByEmail(principal.getName()));
        return "bootstrap/user";
    }
}
