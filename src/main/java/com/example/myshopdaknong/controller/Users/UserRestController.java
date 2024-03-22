package com.example.myshopdaknong.controller.Users;

import com.example.myshopdaknong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;
        @GetMapping("/users/check-username-unique")
        public String checkUserNameUni(@RequestParam("username") String userName)
        {
            System.out.println("check " +this.userService.checkUserNameUni(userName));
            return this.userService.checkUserNameUni(userName);
        }
}
