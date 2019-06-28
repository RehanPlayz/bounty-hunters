package net.Indyuce.bountyhunters.api.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.Indyuce.bountyhunters.BountyUtils;
import net.Indyuce.bountyhunters.api.Bounty;
import net.Indyuce.bountyhunters.api.Message;
import net.Indyuce.bountyhunters.api.PlayerData;

public class BountyClaimEvent extends BountyEvent {
	private Player player;
	
	private static final HandlerList handlers = new HandlerList();

	/*
	 * this event is called whenever a player claims a bounty by killing the
	 * bounty target
	 */
	public BountyClaimEvent(Bounty bounty, Player player) {
		super(bounty);
		this.player = player;
	}

	public Player getClaimer() {
		return player;
	}

	public void sendAllert() {

		// message to player
		Message.CHAT_BAR.format(ChatColor.YELLOW).send(player);
		Message.BOUNTY_CLAIMED_BY_YOU.format(ChatColor.YELLOW, "%target%", getBounty().getTarget().getName(), "%reward%", BountyUtils.format(getBounty().getReward())).send(player);

		// message to server
		PlayerData playerData = PlayerData.get(player);
		String title = playerData.hasTitle() ? ChatColor.LIGHT_PURPLE + "[" + playerData.getTitle() + ChatColor.LIGHT_PURPLE + "] " : "";
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
			if (online != player)
				Message.BOUNTY_CLAIMED.format(ChatColor.YELLOW, "%reward%", BountyUtils.format(getBounty().getReward()), "%killer%", title + player.getName(), "%target%", getBounty().getTarget().getName()).send(online);
		}
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}