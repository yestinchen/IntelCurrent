package com.kernel.intelcurrent.model;

import java.util.LinkedList;
/**
 * classname:Group.java
 * @author 许凌霄
 * */
public class Group 
{
     private static final String TAG=Group.class.getSimpleName();
     
     private String image;
     private String name;
     private String description;
     private LinkedList<User> users;
     
     public Group(String name,String url,String ds)
     {
    	 this.name=name;
    	 image=url;
    	 description=ds;
    	 users=new LinkedList<User>();
     }
     
     public void setName(String s)
     {
    	 name=s;
     }
     
     public void setImage(String url)
     {
    	 image=url;
     }
     
     public void setDescription(String ds)
     {
    	 description=ds;
     }
     
     public String getName()
     {
    	 return name;
     }
     
     public String getDescription()
     {
    	 return description;
     }
     public String getImage()
     {
    	 return image;
     }
     
     public void addUser(User user)
     {
    	 if(hasUser(user.id)==-1)
    	 {
    		 users.add(user);
    	 }
     }
     public boolean removeUser(String id)
     {
    	 int index=hasUser(id);
    	 if(index==-1)
    	 {
    		 return false;
    	 }
    	 else
    	 {
    		 users.remove(index);
    		 return true;
    	 }
     }
     /**
      * 判断分组中有没有给定id的用户，若有返回其索引值，若无返回-1
      * */
     public int hasUser(String id)
     {
    	 int result=-1;
    	 int size=users.size();
    	 for(int i=0;i<size;i++)
    	 {
    		 if(users.get(i).id.equals(id))
    		 {
    			 result=i;
    			 return result;
    		 }
    	 }
    	 return result;
     }
}
