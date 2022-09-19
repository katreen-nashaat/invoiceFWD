/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package invoiceproject.model;

import java.util.ArrayList;

/**
 *
 * @author Home
 */
public class Invoicemaster {

    public int getCustomernumber() {
        return customernumber;
    }

    public ArrayList<Invoicedetails> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Invoicedetails> lines) {
        this.lines = lines;
    }

    public void setCustomernumber(int customernumber) {
        this.customernumber = customernumber;
    }
    
    private int customernumber;

 

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public double getInvoicetotal() {
         int total=0;
       for(Invoicedetails line:lines)
       { 
           total+=line.price*line.count;
           
       }
       invoicetotal=total;
        return invoicetotal;
    }

  
    private String invoicedate;
    private String customername;
    private double invoicetotal;
    ArrayList<Invoicedetails>  lines;
  /*  public Invoicemaster(int invnum,String date,String cusname,Invoicedetails[]  details)
    {
     invoicenumber=invnum;
     invoicedate=date;
     customername=cusname;
     getmylines(details);
     total();
    }*/
    
    public void total()
    {
       int total=0;
       for(Invoicedetails line:lines)
       { 
           total+=line.price;
           
       }
       invoicetotal=total;
       
    }
   /* public  void getmylines(Invoicedetails[] allinvoicedeatails)
    { int i=0;
        for( Invoicedetails line :allinvoicedeatails)
        { 
            if(invoicenumber==line.invoicenumber)
            {
                lines[i]=line;
                i++;
            }
        }
        
    }*/
    
    
    
}
