package com.wufumall.dataexporter.utils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @Explain: 工作当中遇到要读取大数据量Excel（10万行以上，Excel 2007），
 * 用POI方式读取，用HSSFWorkbook读取时，并会内存溢出
 */
public class ExcelImportHelper {

    private List listAll;

    public static final ExcelImportHelper instance;

    static {
        instance = new ExcelImportHelper();
    }

    private ExcelImportHelper() {
        listAll = new ArrayList();
    }
    
    
    /**
     * 功能描述: 非常不建议使用全部加载到内存，
     * 而是在handleRow(int, java.util.List)方法内使用事件驱动
     * @author: chenjy
     * @date: 2017年9月11日 下午4:33:30 
     * @param fileName
     * @return
     */
    public synchronized List getListAll(String fileName) {
        try {
            processOneSheet(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List result = new ArrayList(listAll);
        listAll = new ArrayList();
        return result;
    }

    private void processOneSheet(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        // rId2 found by processing the Workbook
        // Seems to either be rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
        pkg.close();
    }

    private void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
        pkg.close();
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }


    /**
     * 功能描述: 处理每一行
     * @author: chenjy
     * @date: 2017年9月11日 下午4:33:50 
     * @param rowIndex
     * @param celllist
     */
    private void handleRow(int rowIndex, List<String> celllist) {
        listAll.add(rowIndex, celllist);
    }

    private class SheetHandler extends DefaultHandler {
      
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        /**
         * 当前行
         */
        private int rowIndex;
        /**
         * 当前列
         */
        private int columnIndex;

        /**
         * 用于装载每一行
         */
        private List<String> celllist = new ArrayList<String>();

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }


        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            // c => cell
            if (name.equals("c")) {
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            lastContents = "";
        }

        
        
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }
            
            // v => contents of a cell
            if (name.equals("v")) {
                String v = lastContents.trim();
                v = v.equals("") ? " " : v;
                celllist.add(columnIndex, v);
                columnIndex++;
            } else if (name.equals("row")) { // 处理行
              
                if (CollectionUtils.isEmpty(celllist)) return;
                handleRow(rowIndex, new ArrayList<String>(celllist));
                celllist.clear();
                rowIndex++;
                columnIndex = 0;
            }
        }
        
        public void characters(char[] ch, int start, int length)throws SAXException {
            lastContents += new String(ch, start, length);
        }
        
    }

}
