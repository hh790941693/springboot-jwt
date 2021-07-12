package com.pjb.springbootjwt.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CopySqlUtils {

    public static final String TRUNK_BASE_PATH = "G:\\workspace\\mccmcKi\\";

    public static final String XXBF_BASE_PATH = "G:\\pj_mitubishi\\00_document\\04_詳細設計\\202107_変更対応\\現行本番\\";

    public static final String STORE_BASE_PATH = "C:\\Users\\huangchaohui\\Desktop\\三菱项目资料\\202107月三菱需求\\sql\\转换前\\";

    public static final String CC_SQL_PATH = "mccmc\\JavaSource\\jp\\co\\rsi\\mccmc\\cc\\integration\\dao\\impl\\sqlmap\\";

    public static final String COMMON_SQL_PATH = "mccmc\\JavaSource\\jp\\co\\rsi\\mccmc\\common\\dbauto\\integration\\dao\\impl\\sqlmap\\";

    public static final String MC_SQL_PATH = "mccmc\\JavaSource\\jp\\co\\rsi\\mccmc\\mc\\integration\\dao\\impl\\sqlmap\\";

    public static void main(String[] args) {
        run();
    }

    public static void run(){
        List<String> ccFileList = new ArrayList<>(Arrays.asList("CcComExistManager.xml:1","CcComOtherManager.xml:1","CcComPulldownManager.xml:1","CcComSeqNoManager.xml:1"));
        List<String> commonFileList = new ArrayList<>(Arrays.asList("MccChrgCnv.xml","MccExptImptStjk.xml","MccKikiCarCostKs.xml","MccKyori.xml","MccOthrKeihiStjk.xml","MccShashu.xml","MccShkshCostKs.xml",
                                                                    "MccTariffFile.xml","MccTariffXaxis.xml","MccTariff.xml","MccTariffYaxisCd.xml","MccTariffYaxisSuchi.xml","MccTmawsCnd.xml","McmAutoNmb.xml",
                                                                    "McmBinKbn.xml","McmCmnExp.xml","McmCmn.xml","McmGenryo.xml:1","McmHinryaku.xml:1","McmHokanBasho.xml","McmNinushi.xml","McmNisgt.xml","McmNm.xml",
                                                                    "McmOrg.xml","McmReprHin.xml","McmSp.xml","McmTori.xml","MmcNinushiHaifuDetail.xml:1","MmcNinushiHaifu.xml:1","MsnGenba.xml","MsnWkGrp.xml:1",
                                                                    "TccMstHenkoKanri.xml","TcmMepExptJchu.xml"
        ));
        List<String> mcFileList = new ArrayList<>(Arrays.asList("LCCCM07.xml:1","LCMCM08.xml:1","LCMCM75.xml:1","McComExistManager.xml:1","McComOtherManager.xml:1","McComPulldownManager.xml:1","McComSeqNoManager.xml:1"));

        String timeDir = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        for (String cc : ccFileList) {
            if (cc.contains(":")) {
                String filename=cc.split(":")[0];
                copy(XXBF_BASE_PATH+CC_SQL_PATH+filename, STORE_BASE_PATH+timeDir+"\\"+CC_SQL_PATH+filename);
            } else {
                copy(XXBF_BASE_PATH+CC_SQL_PATH+cc, STORE_BASE_PATH+timeDir+"\\modify\\"+CC_SQL_PATH+cc);
            }
        }

        for (String common : commonFileList) {
            if (common.contains(":")) {
                String filename=common.split(":")[0];
                copy(XXBF_BASE_PATH+COMMON_SQL_PATH+filename, STORE_BASE_PATH+timeDir+"\\"+COMMON_SQL_PATH+filename);
            } else {
                copy(XXBF_BASE_PATH+COMMON_SQL_PATH+common, STORE_BASE_PATH+timeDir+"\\modify\\"+COMMON_SQL_PATH+common);
            }
        }

        for (String mc : mcFileList) {
            if (mc.contains(":")) {
                String filename=mc.split(":")[0];
                copy(XXBF_BASE_PATH+MC_SQL_PATH+filename, STORE_BASE_PATH+timeDir+"\\"+MC_SQL_PATH+filename);
            } else {
                copy(XXBF_BASE_PATH+MC_SQL_PATH+mc, STORE_BASE_PATH+timeDir+"\\modify\\"+MC_SQL_PATH+mc);
            }
        }

    }

    public static void copy(String source, String dest) {
        InputStream in = null;
        OutputStream out = null;
        String destPathname = dest.substring(0, dest.lastIndexOf("\\"));
        File destPathFile = new File(destPathname);
        if (!destPathFile.exists()) {
            destPathFile.mkdirs();
        }
        try {
            System.out.println(source + "  -->  " + dest);
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
