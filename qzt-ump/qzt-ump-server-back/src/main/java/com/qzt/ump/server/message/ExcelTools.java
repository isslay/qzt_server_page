package com.qzt.ump.server.message;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.tools.DateTime;
import com.qzt.ump.model.SysParamModel;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelTools {

    @Autowired


    protected WritableSheet ws;

    public String getTfAttenceListExport(HttpServletRequest req, HttpServletResponse res, List<Map> listEnd,String type,String code) throws Exception {
        // List<ChpService> listEnd = chpServiceService.getorderall();

        OutputStream os = res.getOutputStream();// 取得输出流
        res.reset();// 清空输出流
        String xlsName = DateTime.getCurDate_yyyyMMddHHmmss();
        res.setHeader("Content-disposition", "attachment; filename=" + xlsName + ".xls");// 设定输出文件头
        res.setContentType("application/msexcel");// 定义输出类型

        WritableWorkbook wbook = Workbook.createWorkbook(os);

        ws = wbook.createSheet("账单信息", 0);

        WritableFont Titlefont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD,
                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat tilefotmat = new WritableCellFormat(Titlefont);

        //根据参数填写第一行
        SysParamModel sysParamModel = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value()+type);
        String array = sysParamModel.getParamValue();

        //要在map中翻译的名称
        SysParamModel sysCode = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value()+code);
        String arraycode = sysCode.getParamValue();
        //翻译名称
        String[] newArryCode = arraycode.split(":");
        //第一行的名称
        String[] newArry = array.split("_");

        for(int a = 0 ;  a <= newArry.length -1; a++){
            ws.addCell(new Label(a, 0, newArry[a], tilefotmat));
            ws.setColumnView(a, 20);
        }

        for (int i = 1 ; i <= listEnd.size(); i++) {
            ws.addCell(new Label(0, i, String.valueOf(i)));
            Map map = listEnd.get(i-1);
            for (int b = 1 ; b < newArryCode.length+1; b++){

                ws.addCell(new Label(b, i,map.get(newArryCode[b-1])==null?"":map.get(newArryCode[b-1]).toString()));
            }

        }




        


        wbook.write(); // 写入文件
        wbook.close();
        os.close(); // 关闭流

        return null;
    }


}