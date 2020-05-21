package com.pjb.springbootjwt.common.generator.controller;

import com.alibaba.fastjson.JSON;
import com.pjb.springbootjwt.common.generator.domain.TableDO;
import com.pjb.springbootjwt.common.generator.service.GeneratorService;
import com.pjb.springbootjwt.common.generator.type.EnumGen;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.sys.domain.ConfigDO;
import com.pjb.springbootjwt.sys.service.impl.ConfigService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 代码生成
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@RequestMapping("/common/generator")
@Controller
public class GeneratorController {
    String prefix = "common/generator";

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private ConfigService configService;

    @GetMapping()
    String generator() {
        return prefix + "/list";
    }

    @PostMapping("/index")
    public String index(){
        return "common/generator/list.html";
    }

    @ResponseBody
    @GetMapping("/list")
    List<TableDO> list() {
        List<TableDO> list = generatorService.list();
        return list;
    }

    @RequestMapping("/code/{tableName}")
    public void code(HttpServletRequest request, HttpServletResponse response,
                     @PathVariable("tableName") String tableName) throws IOException {
        String[] tableNames = new String[] { tableName };
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+tableName+"_code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @RequestMapping("/batchCode")
    public void batchCode(HttpServletRequest request, HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = tables.split(",");
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        List<ConfigDO> list = configService.findListByKvType(EnumGen.KvType.mapping.getValue());
        List<ConfigDO> list2 = configService.findListByKvType(EnumGen.KvType.base.getValue());
        HashMap<String, String> map = new HashMap<>();
        for(ConfigDO config : list2) {
            map.put(config.getK(), config.getV());
        }
        
        model.addAttribute("list", list);
        model.addAttribute("property", map);
        return prefix + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    Result<String> update(@RequestParam Map<String, String> map) {
        if(!map.containsKey("autoRemovePre")) {
            map.put("autoRemovePre", "false");
        }else {
            map.put("autoRemovePre", "true");
        }
        configService.updateKV(map);
        return Result.ok();
    }
}
