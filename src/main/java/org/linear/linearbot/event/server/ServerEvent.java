package org.linear.linearbot.event.server;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.linear.linearbot.bot.Bot;
import org.linear.linearbot.config.Config;
import org.linear.linearbot.hook.AuthMeHook;
import org.linear.linearbot.tool.StringTool;

import java.util.List;

public class ServerEvent implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        String name = StringTool.filterColor(event.getPlayer().getDisplayName());
        String message = StringTool.filterColor(event.getMessage());

        if (!Config.Forwarding()) {
            return;
        }
        if (AuthMeHook.hasAuthMe) {if (!AuthMeHook.authMeApi.isAuthenticated(event.getPlayer())) {return;} }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups) {
            Bot.sendMsg("[服务器]" + name + ":" + message, groupID);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        String name = StringTool.filterColor(event.getPlayer().getDisplayName());

        if (!Config.JoinAndLeave()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+"加入游戏",groupID);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        String name = StringTool.filterColor(event.getPlayer().getDisplayName());

        if (!Config.JoinAndLeave()){
            return;
        }
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+"退出游戏",groupID);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(!Config.DieReport()){
            return;
        }
        Player player=event.getEntity();
        String displayName= player.getDisplayName();
        String name = StringTool.filterColor(displayName);
        Location location=player.getLocation();
        int x= (int) location.getX();
        int y= (int) location.getY();
        int z= (int) location.getZ();
        String msg = "死在了"+location.getWorld().getName()+"世界"+"("+x+","+y+","+z+")";
        ServerManager.sendCmd("msg "+name+" "+msg);
        List<Long> groups = Config.getGroupQQs();
        for (long groupID : groups){
            Bot.sendMsg("玩家"+name+msg,groupID);
        }
    }
}
