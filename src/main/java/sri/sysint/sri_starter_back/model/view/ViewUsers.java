package sri.sysint.sri_starter_back.model.view;

import java.util.List;

import sri.sysint.sri_starter_back.model.Roles;
import sri.sysint.sri_starter_back.model.Users;

public class ViewUsers {
	  	private Long id;
	    private Integer userId;
	    private String userName;
	    private String email;
	    private String fullName;
	    private Roles roles; 
	    
	    public ViewUsers(Users user, Roles role) {
	    	this.id = user.getId();
	    	this.userId = user.getUserId();
	    	this.email = user.getEmail();
	    	this.userName = user.getUserName();
	    	this.fullName = user.getFullname();
	    	this.roles = role;
	    }
	    
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public Integer getUserId() {
			return userId;
		}
		
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getFullName() {
			return fullName;
		}
		
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		
		public Object getRoles() {
			return roles;
		}
		
		public void setRoles(Roles roles) {
			this.roles = roles;
		}
}
