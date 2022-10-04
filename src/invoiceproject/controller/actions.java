/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package invoiceproject.controller;

import invoiceproject.model.Invoicedetails;
import invoiceproject.model.Invoicemaster;
import invoiceproject.view.invoiceframe;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Home
 */
public class actions  implements ActionListener,TableModelListener,ListSelectionListener
{
    ArrayList<Invoicedetails>objdetails;
ArrayList<Invoicemaster>objheader;
    invoiceframe fram;
      String detailspath;
        String headpath;
    public actions(invoiceframe fram) {
        this.fram=fram;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      
            String command= e.getActionCommand();
             switch (command)
             {
                 case "load":
                     load(null,null);
                     break;
                 case "addinvoice":
                         addinvoice();
                     break;
                 case "deleteinvoice":
                         deleteinvoice();
                     break;
                     case"canceldetail":
                         cancel();
                         break;
                         case "savedetails":
                         save();
                         break;
                        case "saveall":
                                     savefiletest();
                                 break;
                             case "addinvoiceline":
                         addinvoiceline();
                         break;
                        case "deleteinvoiceline":
                                    deleteinvoiceline();
                                 break;
             }
        //JOptionPane.showInputDialog("dddddd");
        
                  
           
    }
    public String[][] load(String inviceheadpath,String invicedetalpath)   {
      /*  File headerFile=null;
        File detailFile=null;
        byte[] detaildata=null;
        byte[] headerdata=null;*/
        String line="";
       // String detailspath;
        //String headpath;
String[][] str = null;
objdetails=new ArrayList<>();
objheader=new ArrayList<>();

    if(inviceheadpath==null&&invicedetalpath==null)
    {JOptionPane.showConfirmDialog(fram, "add invoice line then invoice master ");
        JFileChooser fich=new JFileChooser();
       if(fich.showOpenDialog(null)==JFileChooser.APPROVE_OPTION);
       {
        detailspath=fich.getSelectedFile().toString();
  
            if(fich.showOpenDialog(null)==JFileChooser.APPROVE_OPTION);
       {
           headpath=fich.getSelectedFile().toString();
            
       }             
           
       }
     
        
    }
      else
           {
              headpath=inviceheadpath;
               detailspath=invicedetalpath;
               
               }
    if(headpath!=null&&detailspath!=null)
    {
        try {
                  BufferedReader  buffer = new BufferedReader(new FileReader(detailspath));
                     while((line=buffer.readLine())!=null)
                {
                    String[] values=line.split(",");
//System.out.println(values[0]+" "+values[1]+" "+values[2]);
                   Invoicedetails myobjdetail=new Invoicedetails();
                  String x= values[0];
                    
                    myobjdetail.setName(values[1]);
                    myobjdetail.setInvoicenumber(Integer.parseInt(values[0]));
                    myobjdetail.setPrice(Integer.parseInt(values[2]));
           
                   myobjdetail.setCount(Integer.parseInt(values[3]));
                    
                  objdetails.add(myobjdetail);
                }
                }
                          catch (FileNotFoundException ex) {
                    Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
            }    
          try {
                  BufferedReader  buffer = new BufferedReader(new FileReader(headpath));
                     while((line=buffer.readLine())!=null)
                {
                    String[] values=line.split(",");
//System.out.println(values[0]+" "+values[1]+" "+values[2]);
                   Invoicemaster myobjmaster=new Invoicemaster();
                    myobjmaster.setCustomernumber(Integer.parseInt(values[0]));
                    myobjmaster.setInvoicedate(values[1]);
                    myobjmaster.setCustomername(values[2]);
                  myobjmaster.setLines(getmylines(objdetails,myobjmaster.getCustomernumber()));
                    
                  objheader.add(myobjmaster);
                }
                     str=new String[objheader.size()][4];
//Map  x = new HashMap<>();
for(int i=0;i<objheader.size();i++)
{ 
             str[i][0]=String.valueOf(objheader.get(i).getCustomernumber());
        str[i][1]=objheader.get(i).getCustomername();
            str[i][2]=objheader.get(i).getInvoicedate();
             str[i][3]= String.valueOf(objheader.get(i).getInvoicetotal());
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getCustomernumber()), i, 0);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getCustomername(), i, 1);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getInvoicedate(), i, 2);
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getInvoicetotal()), i, 3);

}
              

                }
                          catch (FileNotFoundException ex) {
                    Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
            }  
    }

    
    return str;
    }
     public  ArrayList<Invoicedetails> getmylines(ArrayList<Invoicedetails> allinvoicedeatails,int num)
    { 
  ArrayList <Invoicedetails> mydetails = new ArrayList<Invoicedetails>();
    
        for( Invoicedetails line :allinvoicedeatails)
        { 
            if(num==line.getInvoicenumber())
            {
               mydetails.add(line);
               
            }
        }
        return mydetails;
        
    }
     public String[] masterheader()
     {
         String[] header={"customer id","date","customer name","total"};
         return header;
     }

    @Override
    public void tableChanged(TableModelEvent e) {
        JOptionPane.showInputDialog("table");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
       //fram.getjTable2().removeAll();
        String[][] str;
         int viewRow = fram.getjTable1().getSelectedRow();
          

        if (!e.getValueIsAdjusting() && viewRow != -1) {

            int columnIndex = 0;

          
            int modelRow = fram.getjTable1().convertRowIndexToModel(viewRow);

         
            Object modelvalue = fram.getjTable1().getModel().getValueAt(modelRow, columnIndex);
            Invoicemaster master=null;
           if(modelvalue!=null)
           {
               
           
             master=getmaster(Integer.parseInt(modelvalue.toString()));
           }
            if(master!=null||modelvalue!=null)
            {
        
            ArrayList<Invoicedetails> listofparentdtl=master.getLines();
                 int rowcount=fram.getjTable2().getModel().getRowCount()-1;
                for(int i=0;i<rowcount;i++)
            {
                fram.getjTable2().getModel().setValueAt(null,i,0);
                fram.getjTable2().getModel().setValueAt(null,i,1);
                fram.getjTable2().getModel().setValueAt(null,i,2);
                fram.getjTable2().getModel().setValueAt(null,i,3);  
            }
            str=new String[listofparentdtl.size()][4];
//Map  x = new HashMap<>();
for(int i=0;i<listofparentdtl.size();i++)
{ 
fram.getInvoicenumber().setText(String.valueOf(master.getCustomernumber()));
 fram.getCustomername().setText(master.getCustomername());
 fram.getInvoicedate().setText(master.getInvoicedate());
 fram.getTotal().setText(String.valueOf(master.getInvoicetotal()));
            
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getInvoicenumber()), i, 0);
fram.getjTable2().getModel().setValueAt(listofparentdtl.get(i).getName(), i, 1);
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getPrice()), i, 2);
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getCount()), i, 3);
}

        
        }
    }
    }
    public Invoicemaster getmaster(int empid)
    {
        for(Invoicemaster master:objheader)
        {
            if(empid==master.getCustomernumber())
            { 
                return master;
            }
        }
        return null;
    }
    public  void addinvoice()
     {
         JPanel panel = new JPanel(new GridLayout(5, 3));
         JLabel customernamel=new JLabel();
            JLabel customeridl=new JLabel();
            JTextField customerdate= new JTextField(10);
         customernamel.setText("customer name");
          customeridl.setText("customer id");
            JLabel customerdatel=new JLabel();
           customerdatel.setText("customer date");
      JTextField customername = new JTextField(10);
     
    
       JTextField customerid= new JTextField(10);
            panel.add(customernamel);
             panel.add(customername);
      panel.add(customerdatel);
       panel.add(customerdate);         
        panel.add(customeridl);
      panel.add(customerid);
     JOptionPane.showMessageDialog(null,panel);
     
      
     Invoicemaster master=new Invoicemaster();
     master.setCustomername(customername.getText());
      master.setCustomernumber(Integer.parseInt(customerid.getText()));
       master.setInvoicedate(customerdate.getText());
        master.setLines(getmylines(objdetails,Integer.parseInt(customerid.getText())));
     
           for(int i=0;i<objheader.size();i++)
            {
                fram.getjTable1().getModel().setValueAt(null,i,0);
                fram.getjTable1().getModel().setValueAt(null,i,1);
                fram.getjTable1().getModel().setValueAt(null,i,2);  
                fram.getjTable1().getModel().setValueAt(null,i,3);  
            }
           objheader.add(master);
           for(int i=0;i<objheader.size();i++)
{ 
          
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getCustomernumber()), i, 0);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getCustomername(), i, 1);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getInvoicedate(), i, 2);
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getInvoicetotal()), i, 3);

}
                JOptionPane.showMessageDialog(null,"added succesfully  ");
           
         
     }
     public void cancel()
     {
         
      
                
                String[][] str;
         int viewRow = fram.getjTable1().getSelectedRow();
          

        if (viewRow != -1) {

            int columnIndex = 0;

          
            int modelRow = fram.getjTable1().convertRowIndexToModel(viewRow);

         
            Object modelvalue = fram.getjTable1().getModel().getValueAt(modelRow, columnIndex);
            Invoicemaster master=null;
           if(modelvalue!=null)
           {
               
           
             master=getmaster(Integer.parseInt(modelvalue.toString()));
           }
            if(master!=null||modelvalue!=null)
            {
        
            ArrayList<Invoicedetails> listofparentdtl=master.getLines();
            str=new String[listofparentdtl.size()][4];
            for(int i=0;i<listofparentdtl.size();i++)
            {
                fram.getjTable2().getModel().setValueAt(null,i,0);
                fram.getjTable2().getModel().setValueAt(null,i,1);
                fram.getjTable2().getModel().setValueAt(null,i,2);
                fram.getjTable2().getModel().setValueAt(null,i,3);
            }
//Map  x = new HashMap<>();
for(int i=0;i<listofparentdtl.size();i++)
{ 
fram.getInvoicenumber().setText(String.valueOf(master.getCustomernumber()));
 fram.getCustomername().setText(master.getCustomername());
 fram.getInvoicedate().setText(master.getInvoicedate());
 fram.getTotal().setText(String.valueOf(master.getInvoicetotal()));
            
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getInvoicenumber()), i, 0);
fram.getjTable2().getModel().setValueAt(listofparentdtl.get(i).getName(), i, 1);
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getPrice()), i, 2);
fram.getjTable2().getModel().setValueAt(String.valueOf(listofparentdtl.get(i).getCount()), i, 3);
}

        
        }
    }
          JOptionPane.showMessageDialog(null,"cancelled succesfully ");
                   
     }
     public void save()
     { /*objdetails=new ArrayList<>();
         Invoicedetails details=new Invoicedetails();
            for(int i=0;i<fram.getjTable2().getModel().getRowCount();i++)
            {
                String invnumber=fram.getjTable2().getModel().getValueAt(i,0 ).toString();
                String price=fram.getjTable2().getModel().getValueAt(i,2 ).toString();
                String count=fram.getjTable2().getModel().getValueAt(i,3 ).toString();
             details.setInvoicenumber(Integer.parseInt(invnumber));
              details.setName((String)fram.getjTable2().getModel().getValueAt(i,1 ));
            details.setPrice(Integer.parseInt(price));
             details.setCount(Integer.parseInt(count));
             objdetails.add(details);            
            }
            JOptionPane.showMessageDialog(null,"saved succesfully ");*/
           String[][] str;
         int viewRow = fram.getjTable1().getSelectedRow();
          

        if (viewRow != -1) {

            int columnIndex = 0;

          
            int modelRow = fram.getjTable1().convertRowIndexToModel(viewRow);

         
            Object modelvalue = fram.getjTable1().getModel().getValueAt(modelRow, columnIndex);
            Invoicemaster master=null;
           if(modelvalue!=null)
           {
               
           
             master=getmaster(Integer.parseInt(modelvalue.toString()));
           }
           Invoicedetails dtls=new Invoicedetails();
            if(master!=null||modelvalue!=null)
            {
        
            ArrayList<Invoicedetails> listofparentdtl=master.getLines();
            for(Invoicedetails dtl:listofparentdtl)
            {
                objdetails.remove(dtl);
                //master.getLines().remove(dtl);
            }
            int rowcount=fram.getjTable2().getModel().getRowCount()-1;
         
            for(int i=0;i<rowcount;i++)
            {
    
                dtls.setInvoicenumber(Integer.parseInt(fram.getjTable2().getModel().getValueAt(i,0).toString()));
                dtls.setName(fram.getjTable2().getModel().getValueAt(i,1).toString());
              dtls.setPrice(Double.parseDouble(fram.getjTable2().getModel().getValueAt(i,2).toString()));
               dtls.setCount(Integer.parseInt(fram.getjTable2().getModel().getValueAt(i,3).toString())); 
               objdetails.add(dtls);    
              // master.getLines().add(dtls);
            }
             for(int i=0;i<rowcount;i++)
            {
                fram.getjTable2().getModel().setValueAt(null,i,0);
                fram.getjTable2().getModel().setValueAt(null,i,1);
                fram.getjTable2().getModel().setValueAt(null,i,2);
                fram.getjTable2().getModel().setValueAt(null,i,3);  
            }
            master.setLines(objdetails);
            
            }
            
         JOptionPane.showConfirmDialog(fram, "saved succesfully");
     }
     }
      public void savedetils()
     { 
         
     }
           
           
     
         
     
    
     public void deleteinvoice()
     {
         String[][] str;
         int viewRow = fram.getjTable1().getSelectedRow();
          

        if ( viewRow != -1) {

            int columnIndex = 0;

          
            int modelRow = fram.getjTable1().convertRowIndexToModel(viewRow);

         
            Object modelvalue = fram.getjTable1().getModel().getValueAt(modelRow, columnIndex);
            Invoicemaster master=null;
           if(modelvalue!=null)
           {
                for(int i=0;i<objheader.size();i++)
            {
                fram.getjTable1().getModel().setValueAt(null,i,0);
                fram.getjTable1().getModel().setValueAt(null,i,1);
                fram.getjTable1().getModel().setValueAt(null,i,2);
                fram.getjTable1().getModel().setValueAt(null,i,3);
            }
                        
             master=getmaster(Integer.parseInt(modelvalue.toString()));
            objheader.remove(master);
            
              
           
            for(int i=0;i<objheader.size();i++)
{ 
          
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getCustomernumber()), i, 0);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getCustomername(), i, 1);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getInvoicedate(), i, 2);
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getInvoicetotal()), i, 3);
}

            
           }
           
           
     }
        JOptionPane.showConfirmDialog(fram, "deleted succesfully");
}
/*    public void savefile()
     {
          File csvOutputFile = new File(headpath);
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
        objheader.stream()
          .map(this::convertToCSV)
          .forEach(pw::println);
     }  catch (FileNotFoundException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
}*/
      public  void addinvoiceline()
     {
         JPanel panel = new JPanel(new GridLayout(5, 3));
         JLabel itemnamel=new JLabel();
            JLabel customeridl=new JLabel();
            JTextField price= new JTextField(10);
        itemnamel.setText("item name");
          customeridl.setText("customer id");
            JLabel pricel=new JLabel();
           pricel.setText("price");
      JTextField itemname = new JTextField(10);
         JLabel countl=new JLabel();
           pricel.setText("price");
           countl.setText("count");
      JTextField count= new JTextField(10);
     
    
       JTextField customerid= new JTextField(10);
        panel.add(customeridl);
      panel.add(customerid);
            panel.add(itemnamel);
             panel.add(itemname);
      panel.add(pricel);
       panel.add(price);
        panel.add(countl);
       panel.add(count);
       
       
     JOptionPane.showMessageDialog(null,panel);
     
      
     Invoicedetails detail=new Invoicedetails();
    detail.setName(itemname.getText());
      detail.setCount(Integer.parseInt(count.getText()));
       detail.setInvoicenumber(Integer.parseInt(customerid.getText()));
           detail.setPrice(Double.parseDouble(price.getText()));

     
           /*for(int i=0;i<objdetails.size();i++)
            {
                fram.getjTable2().getModel().setValueAt(null,i,0);
                fram.getjTable2().getModel().setValueAt(null,i,1);
                fram.getjTable2().getModel().setValueAt(null,i,2);  
                fram.getjTable2().getModel().setValueAt(null,i,3);  
            }*/
          objdetails.add(detail);
          /* for(int i=0;i<objheader.size();i++)
{ 
          
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getCustomernumber()), i, 0);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getCustomername(), i, 1);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getInvoicedate(), i, 2);
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getInvoicetotal()), i, 3);

}*/
          for(int i=0;i<objheader.size();i++)
          { Invoicemaster objmaster=objheader.get(i);
          objmaster.setLines(getmylines(objdetails,objmaster.getCustomernumber()));
          objmaster.total();
               
          }
           JOptionPane.showMessageDialog(null," item added succesfully reselect invoice master ");
         
     }
       public void deleteinvoiceline()
     {
         String[][] str;
         int viewRow = fram.getjTable2().getSelectedRow();
          

        if ( viewRow != -1) {

            int columnIndex = 0;

          
            int modelRow = fram.getjTable2().convertRowIndexToModel(viewRow);
Invoicedetails details=new Invoicedetails();
         
            Object modelvalue = fram.getjTable2().getModel().getValueAt(modelRow, columnIndex);
            int invoicenumber=Integer.parseInt(modelvalue.toString()); 
            
                  details.setInvoicenumber(invoicenumber);
            details.setCount(Integer.parseInt(fram.getjTable2().getModel().getValueAt(modelRow,3).toString()));
            details.setPrice(Double.parseDouble(fram.getjTable2().getModel().getValueAt(modelRow,2).toString()));
            details.setName(fram.getjTable2().getModel().getValueAt(modelRow,1).toString());
                       if(modelvalue!=null)
           {
               /* for(int i=0;i<objheader.size();i++)
            {
                fram.getjTable1().getModel().setValueAt(null,i,0);
                fram.getjTable1().getModel().setValueAt(null,i,1);
                fram.getjTable1().getModel().setValueAt(null,i,2);
                fram.getjTable1().getModel().setValueAt(null,i,3);
            }*/
                        
             //master=getmaster(Integer.parseInt(modelvalue.toString()));
                      for(int i=0;i<objdetails.size();i++)
          { Invoicedetails objdtl=objdetails.get(i);
          if(objdtl.getInvoicenumber()==details.getInvoicenumber()&&objdtl.getName()==details.getName()&&objdtl.getPrice()==details.getPrice()&&objdtl.getCount()==details.getCount())
                  {   
                      details=objdtl;
                       objdetails.remove(details);
                  }        
                  
               
          }
           // objdetails.remove(details);
               ArrayList<Invoicedetails> inv=objdetails;
              
           for(int i=0;i<objheader.size();i++)
          { Invoicemaster objmaster=objheader.get(i);
          objmaster.setLines(getmylines(objdetails,objmaster.getCustomernumber()));
          objmaster.total();
               
          }
            /*for(int i=0;i<objheader.size();i++)
{ 
          
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getCustomernumber()), i, 0);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getCustomername(), i, 1);
fram.getjTable1().getModel().setValueAt(objheader.get(i).getInvoicedate(), i, 2);
fram.getjTable1().getModel().setValueAt(String.valueOf(objheader.get(i).getInvoicetotal()), i, 3);
}*/

            
           }
           
           
     }
        JOptionPane.showConfirmDialog(fram, "deleted succesfully reselct invoice master to view changes  ");
}
     /*public void savefile()
     {
         BufferedWriter br=null;
        try {
            br = new BufferedWriter(new FileWriter("headpath"));
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
StringBuilder sb = new StringBuilder();
String str[][]=new String[objheader.size()][4];
for(int i=0;i<objheader.size();i++)
{ 
             str[i][0]=String.valueOf(objheader.get(i).getCustomernumber());
        str[i][1]=objheader.get(i).getCustomername();
            str[i][2]=objheader.get(i).getInvoicedate();
             str[i][3]= String.valueOf(objheader.get(i).getInvoicetotal());

}
for ( String[] element : str) {
sb.append(element[0]);
sb.append(",");
sb.append(element[1]);
sb.append(",");
sb.append(element[2]);
sb.append(",");
sb.append("\n");

}

        try {
            br.write(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
            br = new BufferedWriter(new FileWriter(detailspath));
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
StringBuilder sbdet = new StringBuilder();
String detstr[][]=new String[objdetails.size()][4];
for(int i=0;i<objdetails.size();i++)
{ 
             detstr[i][0]=String.valueOf(objdetails.get(i).getInvoicenumber());
        detstr[i][1]=objdetails.get(i).getName();
           detstr[i][2]=String.valueOf(objdetails.get(i).getPrice());
             detstr[i][3]= String.valueOf(objdetails.get(i).getCount());

}
for ( String[] element : detstr) {
sb.append(element[0]);
sb.append(",");
sb.append(element[1]);
sb.append(",");
sb.append(element[2]);
sb.append(",");
sb.append(element[3]);
sb.append(",");
sb.append("\n");

}

        try {
            br.write(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showConfirmDialog(fram, "saved succesfully");
     }
   */

 public void savefiletest()
     {
           // String headerspath="E:\\FWD\\InvoiceHeader.csv";
             // String linepath="E:\\FWD\\InvoiceLine.csv";
         BufferedWriter br=null;
         BufferedWriter brdet=null;
        try {
            br = new BufferedWriter(new FileWriter(headpath));
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
StringBuilder sb = new StringBuilder();
String str[][]=new String[objheader.size()][4];
ArrayList<Invoicemaster> mas=objheader;
for(int i=0;i<objheader.size();i++)
{ 
             str[i][0]=String.valueOf(objheader.get(i).getCustomernumber());
        str[i][1]=objheader.get(i).getCustomername();
            str[i][2]=objheader.get(i).getInvoicedate();
             str[i][3]= String.valueOf(objheader.get(i).getInvoicetotal());

}
for ( String[] element : str) {
sb.append(element[0]);
sb.append(",");
sb.append(element[1]);
sb.append(",");
sb.append(element[2]);
sb.append(",");
sb.append("\n");

}

        try {
            br.write(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
            brdet = new BufferedWriter(new FileWriter(detailspath));
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
StringBuilder sbdet = new StringBuilder();
String detstr[][]=new String[objdetails.size()][4];
for(int i=0;i<objdetails.size();i++)
{ 
             detstr[i][0]=String.valueOf(objdetails.get(i).getInvoicenumber());
        detstr[i][1]=objdetails.get(i).getName();
           detstr[i][2]=String.valueOf(objdetails.get(i).getPrice());
             detstr[i][3]= String.valueOf(objdetails.get(i).getCount());

}
for ( String[] element : detstr) {
sbdet.append(element[0]);
sbdet.append(",");
sbdet.append(element[1]);
sbdet.append(",");
sbdet.append(element[2]);
sbdet.append(",");
sbdet.append(element[3]);
sbdet.append(",");
sbdet.append("\n");

}

        try {
            brdet.write(sbdet.toString());
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            brdet.close();
        } catch (IOException ex) {
            Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showConfirmDialog(fram, "saved succesfully");
     }
   
}

 /*  try {
                FileInputStream fis=new FileInputStream(detailFile);
                int size=fis.available();
                 detaildata=new byte[size];
                fis.read(detaildata);
                     
            } catch (FileNotFoundException ex) {
                Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(actions.class.getName()).log(Level.SEVERE, null, ex);
            }*/