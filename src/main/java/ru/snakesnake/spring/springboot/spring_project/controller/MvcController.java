package ru.snakesnake.spring.springboot.spring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.snakesnake.spring.springboot.spring_project.model.Role;
import ru.snakesnake.spring.springboot.spring_project.model.User;
import ru.snakesnake.spring.springboot.spring_project.service.RoleServiceImpl;
import ru.snakesnake.spring.springboot.spring_project.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/")
public class MvcController {

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
