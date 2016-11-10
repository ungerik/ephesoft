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

package com.ephesoft.dcma.user.connectivity.constant;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class RVTest {

	public static void main(String[] args) {
		RVTest retrieveUserAttributes = new RVTest();
		retrieveUserAttributes.getUserBasicAttributes("ephesoft", retrieveUserAttributes.getLdapContext());
	}

	public LdapContext getLdapContext(){
	        LdapContext ctx = null;
	        try{
	            Hashtable<String,String> env = new Hashtable<String, String>();
	            env.put(Context.INITIAL_CONTEXT_FACTORY,
	                    "com.sun.jndi.ldap.LdapCtxFactory");
	            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
	            env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=ephesoft,dc=com");
	            env.put(Context.SECURITY_CREDENTIALS, "secret");
	            env.put(Context.PROVIDER_URL, "ldap://localhost:389");
	            ctx = new InitialLdapContext(env, null);
	            System.out.println("Connection Successful.");
	        }catch(NamingException nex){
	            System.out.println("LDAP Connection: FAILED");
	            nex.printStackTrace();
	        }
	        return ctx;
	    }

	private void getUserBasicAttributes(String username, LdapContext ctx) {
	
		try {

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = {"distinguishedName", "sn", "givenname", "mail", "telephonenumber"};
			constraints.setReturningAttributes(attrIDs);
			// First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
			// Second Attribute can be uid=username
			NamingEnumeration answer = ctx.search("dc=ephesoft,dc=com", "cn=" + username, constraints);
			if (answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				System.out.println("distinguishedName " + attrs.get("distinguishedName"));
				System.out.println("givenname " + attrs.get("givenname"));
				System.out.println("sn " + attrs.get("sn"));
				System.out.println("mail " + attrs.get("mail"));
				System.out.println("telephonenumber " + attrs.get("telephonenumber"));
			} else {
				throw new Exception("Invalid User");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
