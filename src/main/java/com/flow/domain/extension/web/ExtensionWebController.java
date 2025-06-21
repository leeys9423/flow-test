package com.flow.domain.extension.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/extensions")
@RequiredArgsConstructor
public class ExtensionWebController {

    @GetMapping
    public String extensionsPage() {
        return "extensions";
    }
}
