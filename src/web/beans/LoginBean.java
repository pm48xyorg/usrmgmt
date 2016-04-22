/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import common.remote.AuthServer;

import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import server.remoteimpl.AuthServerImpl;

/**
 *
 * @author user
 */
@ManagedBean
@ViewScoped
public class LoginBean {
    
    private String userId;
    private String passwd;
    private String saltKey;
 
    
    public LoginBean(){
    //saltKey = new Date().getTime()+"";
    
        Random random = new Random();
        saltKey =  random.nextInt()+"";
        HttpSession session =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("saltKeyCopy", saltKey);
    }
    public String doLogin(){
    
        AuthServer authServer = new AuthServerImpl();
        HttpSession session =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String saltKeyCopy = (String)session.getAttribute("saltKeyCopy");
        String result[] = authServer.vaidateUser_UserManagemtnt(userId, passwd, saltKeyCopy);
         
        System.out.println("result" + result[0] + "  " + result[1]);
        if(result[0].equalsIgnoreCase("success")){
            System.out.println("returingin success");
            
            HttpSession sessionOld =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            System.out.println("sessionOld.getId()"+sessionOld.getId());
            
            sessionOld.invalidate();
            HttpSession sessionNew =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            sessionNew.setAttribute("userId",userId);
            System.out.println("sessionNew.getId()"+sessionNew.getId());
            
        return "success";
        }
        else{
            // error message add
             Random random = new Random();
        saltKey =  random.nextInt()+"";
       
        HttpSession session1 =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session1.setAttribute("saltKeyCopy", saltKey);
        passwd="";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, result[1],result[1]);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        return "failed";
        }
    }
public String doLogout(){
   HttpSession sessionOld =(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            System.out.println("sessionOld.getId()"+sessionOld.getId());
            sessionOld.invalidate();
            // do more stuff.
            // like audit trail.
        return "/index.xhtml?faces-redirect=true";
}
    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the passwd
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * @param passwd the passwd to set
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * @return the saltKey
     */
    public String getSaltKey() {
        return saltKey;
    }

    /**
     * @param saltKey the saltKey to set
     */
    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
    }
    
    
}
