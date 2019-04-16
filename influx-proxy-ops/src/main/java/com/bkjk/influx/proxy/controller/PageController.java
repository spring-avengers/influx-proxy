package com.bkjk.influx.proxy.controller;

import com.bkjk.influx.proxy.entity.BackendNodePo;
import com.bkjk.influx.proxy.entity.KeyMappingPo;
import com.bkjk.influx.proxy.entity.ManagementInfoPo;
import com.bkjk.influx.proxy.service.BackendNodeService;
import com.bkjk.influx.proxy.service.KeyMappingService;
import com.bkjk.influx.proxy.service.ManagementInfoService;
import com.bkjk.influx.proxy.util.IdentityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Controller
public class PageController {

    @Resource
    private BackendNodeService backendNodeService;

    @Resource
    private KeyMappingService keyMappingService;

    @Resource
    private ManagementInfoService managementInfoService;

    @GetMapping("/t")
    @ResponseBody
    public Object t(@RequestHeader("x-id") String id){
        return id;
    }

    @GetMapping({"/", ""})
    public String welcome(Model model) {
        return "redirect:/index";
    }

    @GetMapping({"/index"})
    public String index(Model model, HttpServletRequest request) {
        String user = IdentityUtils.getUserNameDN();
        if(Optional.ofNullable(user).isPresent()){
            model.addAttribute("userName", user);
        }
        return "index";
    }

    @GetMapping({"/node/list"})
    public String nodeList() {
        return "node/list";
    }

    @GetMapping({"/node/add"})
    public String nodeAdd(Model model) {
        return "node/add";
    }

    @GetMapping("/node/edit/{id}")
    public String nodeEdit(@PathVariable("id") Long id, Model model) {
        BackendNodePo backendNodePo = backendNodeService.getBackendNodeById(id);
        model.addAttribute("node", backendNodePo);
        return "node/edit";
    }

    @GetMapping({"/mapping/list"})
    public String mappingList(Model model) {
        return "mapping/list";
    }

    @GetMapping({"/mapping/add"})
    public String mappingAdd(Model model) {
        return "mapping/add";
    }

    @GetMapping({"/mapping/edit/{id}"})
    public String mappingEdit(@PathVariable("id") Long id, Model model) {
        KeyMappingPo keyMapping = keyMappingService.getKeyMappingById(id);
        model.addAttribute("mapping", keyMapping);
        return "mapping/edit";
    }

    @GetMapping({"/retention/list"})
    public String retentionList(Model model) {
        return "retention/list";
    }

    @GetMapping({"/retention/add"})
    public String retentionAdd(Model model) {
        return "retention/add";
    }

    @GetMapping({"/retention/edit/{id}"})
    public String retentionEdit(@PathVariable("id") Long id, Model model) {
        ManagementInfoPo managementInfoPo = managementInfoService.selectRetentionById(id);
        model.addAttribute("retention", managementInfoPo);
        return "retention/edit";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
