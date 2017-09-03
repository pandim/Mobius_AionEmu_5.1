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
package system.handlers.quest.high_daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _25321The_Grace_Of_The_Mace extends QuestHandler
{
	public static final int questId = 25321;
	// Enshar
	private static final int[] DF5_P1 =
	{
		219693,
		219694,
		219695,
		219696,
		219697,
		219698
	};
	private static final int[] DF5_P2 =
	{
		219778,
		219779,
		219780,
		219781,
		219782,
		219783,
		219784,
		219786
	};
	// Gelkmaros [Conquest Offering]
	private static final int[] DF4_Rotation =
	{
		236586,
		236587,
		236588,
		236589,
		236590,
		236591,
		236592,
		236593,
		236594,
		236595,
		236596,
		236597,
		236598,
		236599,
		236600,
		236601,
		236602,
		236603,
		236604,
		236605,
		236606,
		236607,
		236608,
		236609
	};
	// Levinshor
	private static final int[] LDF4_Advance =
	{
		234695,
		234696,
		234697,
		234700,
		234701,
		234702,
		234703
	};
	// Kaldor
	private static final int[] LDF5_Fortress =
	{
		234244,
		234246,
		234247,
		234503,
		234505,
		234504,
		234517,
		234518,
		234519,
		234520,
		234521,
		234522,
		234523,
		234524,
		234525,
		234526,
		234527,
		234528
	};
	// Reshanta [Upper Abyss]
	private static final int[] AB1 =
	{
		883301,
		883302,
		883303,
		883304,
		883305,
		883306,
		883307,
		883308
	};
	
	public _25321The_Grace_Of_The_Mace()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805342).addOnQuestStart(questId); // Hikait.
		qe.registerQuestNpc(805342).addOnTalkEvent(questId); // Hikait.
		qe.registerQuestNpc(805344).addOnTalkEvent(questId); // Nuiage.
		qe.registerQuestNpc(805345).addOnTalkEvent(questId); // Sturumwind.
		qe.registerQuestNpc(805346).addOnTalkEvent(questId); // Korperchen.
		qe.registerQuestNpc(805347).addOnTalkEvent(questId); // Fenke.
		qe.registerQuestNpc(805348).addOnTalkEvent(questId); // Sach.
		qe.registerQuestNpc(805349).addOnTalkEvent(questId); // Jelewoe.
		for (final int mob : DF5_P1)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (final int mob2 : DF5_P2)
		{
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
		for (final int mob3 : DF4_Rotation)
		{
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
		}
		for (final int mob4 : LDF4_Advance)
		{
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
		}
		for (final int mob5 : LDF5_Fortress)
		{
			qe.registerQuestNpc(mob5).addOnKillEvent(questId);
		}
		for (final int mob6 : AB1)
		{
			qe.registerQuestNpc(mob6).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("DRAGONREST_TEMPLE_220080000"), questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (zoneName == ZoneName.get("DRAGONREST_TEMPLE_220080000"))
		{
			if ((qs == null) || qs.canRepeat())
			{
				env.setQuestId(questId);
				if (QuestService.startQuest(env))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 805344)
			{ // Nuiage.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case SELECT_ACTION_1012:
					{
						return sendQuestDialog(env, 1012);
					}
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 805345)
			{ // Sturumwind.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1693);
					}
					case SELECT_ACTION_1694:
					{
						return sendQuestDialog(env, 1694);
					}
					case STEP_TO_3:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 805346)
			{ // Korperchen.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 2375);
					}
					case SELECT_ACTION_2376:
					{
						return sendQuestDialog(env, 2376);
					}
					case STEP_TO_5:
					{
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 805347)
			{ // Fenke.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 3057);
					}
					case SELECT_ACTION_3058:
					{
						return sendQuestDialog(env, 3058);
					}
					case STEP_TO_7:
					{
						changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 805348)
			{ // Sach.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 3739);
					}
					case SELECT_ACTION_3740:
					{
						return sendQuestDialog(env, 3740);
					}
					case STEP_TO_9:
					{
						changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 805349)
			{ // Jelewoe.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 6500);
					}
					case SELECT_ACTION_6501:
					{
						return sendQuestDialog(env, 6501);
					}
					case STEP_TO_11:
					{
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805342)
			{ // Hikait.
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
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
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);
		final int var1 = qs.getQuestVarById(1);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		if ((var == 1) && (var1 >= 0) && (var1 < 29))
		{
			return defaultOnKillEvent(env, DF5_P1, var1, var1 + 1, 1);
		}
		else if ((var == 1) && (var1 == 29))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 3) && (var1 >= 0) && (var1 < 29))
		{
			return defaultOnKillEvent(env, DF5_P2, var1, var1 + 1, 1);
		}
		else if ((var == 3) && (var1 == 29))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 5) && (var1 >= 0) && (var1 < 9))
		{
			return defaultOnKillEvent(env, DF4_Rotation, var1, var1 + 1, 1);
		}
		else if ((var == 5) && (var1 == 9))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 7) && (var1 >= 0) && (var1 < 29))
		{
			return defaultOnKillEvent(env, LDF4_Advance, var1, var1 + 1, 1);
		}
		else if ((var == 7) && (var1 == 29))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 7, 8, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 9) && (var1 >= 0) && (var1 < 29))
		{
			return defaultOnKillEvent(env, LDF5_Fortress, var1, var1 + 1, 1);
		}
		else if ((var == 9) && (var1 == 29))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 9, 10, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 11) && (var1 >= 0) && (var1 < 29))
		{
			return defaultOnKillEvent(env, AB1, var1, var1 + 1, 1);
		}
		else if ((var == 11) && (var1 == 29))
		{
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(12);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}