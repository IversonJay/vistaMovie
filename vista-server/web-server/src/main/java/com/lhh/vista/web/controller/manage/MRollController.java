package com.lhh.vista.web.controller.manage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.FileTool;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.dto.TicketInfo;
import com.lhh.vista.service.model.Roll;
import com.lhh.vista.service.service.RollService;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Sequence;
import com.lhh.vista.temp.service.CinemaService;
import com.lhh.vista.temp.service.SessionService;
import com.lhh.vista.temp.service.TicketInfoService;
import com.lhh.vista.web.common.CommonData;

@Controller
@RequestMapping("m_roll")
public class MRollController extends BaseController {
    @Autowired
    private RollService rollService;
    @Autowired 
    private SessionService sessionService;

    /**
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("toEdit")
    @ResponseBody
    public ResponseResult<String> toPage() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	List<Sequence> slist = sessionService.getAll();
        	Map<String, Object> map = new HashMap<>();
        	map.put("slist", slist);
            r.setResult(getContent("manage/edit_roll.ftl", map));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        } catch (Exception e) {
            r.setState(StateTool.State.FAIL);
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取分页
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public ResponseResult<PagerResponse<Roll>> getPager(PagerRequest pager) {
        ResponseResult<PagerResponse<Roll>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(rollService.getPager(pager, null));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 保存信息
     *
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResponseResult<Integer> save(Roll roll, @RequestParam("coverFileTemp") MultipartFile coverFileTemp) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            String saveRealPath = System.getProperty("vistaapp-web.root") + CommonData.ROLL_FILE_PATH;
            String savePath = CommonData.ROLL_FILE_PATH;
            // 检查目录
            File uploadDir = new File(saveRealPath);
            uploadDir.mkdirs();
            if (uploadDir.isDirectory() && uploadDir.canWrite() && !coverFileTemp.isEmpty()) {
                String originalFileName = coverFileTemp.getOriginalFilename();
                String fileName = UUID.randomUUID().toString().replace("-", "");

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(coverFileTemp.getBytes()));
                if (bufferedImage.getHeight(null) / bufferedImage.getWidth(null) != 2) {
                    int w = 800;
                    int h = 400;
                    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics graphics = image.createGraphics();
                    graphics.drawImage(bufferedImage, 0, 0, w, h, null);
                    String namePath = saveRealPath + fileName + ".png";
                    File mimg = new File(namePath);
                    mimg.createNewFile();
                    ImageIO.write(image, "png", mimg);
                    roll.setCoverPath(savePath + fileName + ".png");
                } else {
                    String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                    FileTool.saveFileFromBytes(coverFileTemp.getBytes(), saveRealPath + fileName + "." + fileExt);
                    roll.setCoverPath(savePath + fileName + "." + fileExt);
                }
            }
            System.out.println(roll.getCoverPath());
            r.setResult(rollService.save(roll));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception se) {
            r.setState(StateTool.State.FAIL);
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public BaseResult del(Integer id) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            rollService.del(id);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 获取单个信息
     *
     * @return
     */
    @RequestMapping("find")
    @ResponseBody
    public ResponseResult<Roll> find(Integer id) {
        ResponseResult<Roll> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(rollService.find(id));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
