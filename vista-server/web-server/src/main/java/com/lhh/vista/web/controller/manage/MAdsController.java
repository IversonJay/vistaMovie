package com.lhh.vista.web.controller.manage;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.FileTool;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Ads;
import com.lhh.vista.service.model.Roll;
import com.lhh.vista.service.service.AdsService;
import com.lhh.vista.service.service.RollService;
import com.lhh.vista.web.common.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

@Controller
@RequestMapping("m_ads")
public class MAdsController extends BaseController {
    @Autowired
    private RollService rollService;
    @Autowired
    private AdsService adsService;

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
            r.setResult(getContent("manage/edit_ads.ftl", null));
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
    public ResponseResult<PagerResponse<Ads>> getPager(PagerRequest pager) {
        ResponseResult<PagerResponse<Ads>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(adsService.getPager(pager, null));
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
    public ResponseResult<Integer> save(Ads ads, @RequestParam("coverFileTemp") MultipartFile coverFileTemp) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            String saveRealPath = System.getProperty("vistaapp-web.root") + CommonData.ADS_FILE_PATH;
            String savePath = CommonData.ADS_FILE_PATH;
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
                    ads.setCoverPath(savePath + fileName + ".png");
                } else {
                    String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                    FileTool.saveFileFromBytes(coverFileTemp.getBytes(), saveRealPath + fileName + "." + fileExt);
                    ads.setCoverPath(savePath + fileName + "." + fileExt);
                }
            }
            System.out.println(ads.getCoverPath());
            r.setResult(adsService.save(ads));
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
        	adsService.del(id);
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
    public ResponseResult<Ads> find(Integer id) {
        ResponseResult<Ads> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(adsService.find(id));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
