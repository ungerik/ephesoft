/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction;

import java.util.LinkedList;
import java.util.Random;

import com.ephesoft.gxt.core.shared.util.StringUtil;


public class ColorGenerator {
	private Random randomRed ;
	private Random randomGreen;
	private Random randomBlue;
	int count=0;
	private final String RGBCONSTANT="rgb";
	private final String OPENBRACKET="(";
	private final String CLOSEBRACKET=")";
	private final String COMMA=",";
	
	   public ColorGenerator() {
	       this.randomRed = new Random(10);
	       this.randomGreen = new Random(20);
	       this.randomBlue = new Random(30);
	   }

	   public synchronized LinkedList<Integer> getNext() {
		   LinkedList<Integer> rgb=new LinkedList<Integer>();
		   for(int i=0;i<3;i++){
			   Double randomNum=randomGreen.nextDouble()*255;
			   rgb.add(randomNum.intValue());
		   }
	        return rgb;
	   }
	   public synchronized int getNextRed(){
		   Double red=randomRed.nextDouble()*255;
		   return red.intValue();
	   }
	   public synchronized int getNextBlue(){
		   Double blue=randomBlue.nextDouble()*255;
		   return blue.intValue();
	   }
	   public synchronized int getNextGreen(){
		   Double green=randomGreen.nextDouble()*255;
		   return green.intValue();
	   }
	   public synchronized String getNextColor(){
		   String colorCode="";
		   if(count==0){
			   colorCode=generateColorCode(255, 0, 0);
		   }else if(count==1){
			   colorCode=generateColorCode(0, 255, 0);
		   }else if(count==2){
			   colorCode=generateColorCode(0, 0, 255);
		   }else{
			   colorCode=generateColorCode(getNextRed(), getNextGreen(), getNextBlue());
		   }
		   count++;
		   return colorCode;
	   }
	   public synchronized String generateColorCode(int red, int green, int blue){
		   return StringUtil.concatenate(RGBCONSTANT,OPENBRACKET,red,COMMA,green,COMMA, blue, CLOSEBRACKET);

	   }
}
