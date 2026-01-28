package com.morocco2030.app.web.rest;

import com.morocco2030.app.domain.User;
import com.morocco2030.app.repository.UserRepository;
import com.morocco2030.app.security.SecurityUtils;
import com.morocco2030.app.service.MailService;
import com.morocco2030.app.service.UserService;
import com.morocco2030.app.service.dto.AdminUserDTO;
import com.morocco2030.app.service.dto.PasswordChangeDTO;
import com.morocco2030.app.web.rest.errors.*;
import com.morocco2030.app.web.rest.vm.KeyAndPasswordVM;
import com.morocco2030.app.web.rest.vm.ManagedUserVM;
import jakarta.validation.Valid;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for testing server availability.
 */
@RestController
@RequestMapping("/status")
public class PingResource {

    @GetMapping("ping")
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        return "Pong :)";
    }
}
