package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class lab1_2 {
    
    public static void main(String[] args) throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("../test.c"));
        String readString;
        String regexPatternStr = "abstract  default  goto*  switch  boolean  do  if  package  nchronzed  break  double  implements  private  this  byte  else  import  protected  throw  throws  case  extends  instanceof  public  transient  catch  int  return  char  final  interface  short  try  class  finally  long  static  void  const*  float  native  strictfp  volatile  continue  for  new  super  while  assert  enum";
        String[] regexPatternArr = regexPatternStr.split(" ");
        StringBuffer readStringBuffer = new StringBuffer();
        while ((readString = bReader.readLine()) != null) {
            readStringBuffer.append(readString);
        }

    }


    public void findByRegex(String regex) {
        
    }
    

}
