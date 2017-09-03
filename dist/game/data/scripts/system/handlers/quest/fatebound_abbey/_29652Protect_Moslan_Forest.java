/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package system.handlers.quest.fatebound_abbey;

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _29652Protect_Moslan_Forest extends QuestHandler
{
	private static final int questId = 29652;
	private static final int[] mobs =
	{
		210656,
		210657,
		210425,
		210426,
		210427
	};
	
	public _29652Protect_Moslan_Forest()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804662).addOnQuestStart(questId);
		qe.registerQuestNpc(804662).addOnTalkEvent(questId);
		qe.registerQuestNpc(804662).addOnTalkEvent(questId);
		for (final int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 804662)
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						if (player.getInventory().getItemCountByItemId(164000336) >= 1)
						{ // Abbey Return Stone.
							return sendQuestDialog(env, 4762);
						}
						else
						{
							PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You must have <Abbey Return Stone>", ChatType.BRIGHT_YELLOW_CENTER), true);
							return true;
						}
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
						return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 804662:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 10002);
						}
						case SELECT_REWARD:
						{
							return sendQuestEndDialog(env);
						}
						default:
							return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804662)
			{
				switch (dialog)
				{
					case SELECT_REWARD:
					{
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		switch (targetId)
		{
			case 210425:
			case 210426:
			case 210427:
			case 210656:
			case 210657:
				if (qs.getQuestVarById(1) < 10)
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				if (qs.getQuestVarById(1) >= 10)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
				break;
		}
		return false;
	}
}