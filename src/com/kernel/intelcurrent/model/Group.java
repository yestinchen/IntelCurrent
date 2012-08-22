package com.kernel.intelcurrent.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
/**
 * classname:Group.java
 * @author 许凌霄
 * */
public class Group implements Serializable
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 5064825253786288366L;

	private static final String TAG=Group.class.getSimpleName();
     
     public String name;
     public String image;
     private String description;
     public LinkedList<SimpleUser> users = new LinkedList<SimpleUser>();
     
     public Group(){}
     
     public Group(String name,String url,String ds)
     {
    	 this.name=name;
    	 image=url;
    	 description=ds;
    	 users=new LinkedList<SimpleUser>();
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
     
     public void addUser(SimpleUser user)
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

	@Override
	public String toString() {
		return "Group [image=" + image + ", name=" + name + ", description="
				+ description + ", users=" + users + "]";
	}
     
	public static class NameComparator implements Comparator<Group>{
		@Override
		public int compare(Group lhs, Group rhs) {
			return lhs.name.compareTo(rhs.name);
		}
		
	}
     
}
