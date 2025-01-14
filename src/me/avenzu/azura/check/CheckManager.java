package me.avenzu.azura.check;

import java.util.Deque;
import java.util.LinkedList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.avenzu.azura.check.checks.combat.aimassist.AimAssistA;
import me.avenzu.azura.check.checks.combat.aura.AuraA;
import me.avenzu.azura.check.checks.combat.aura.AuraB;
import me.avenzu.azura.check.checks.combat.autoclicker.AutoClickerA;
import me.avenzu.azura.check.checks.combat.reach.ReachA;
import me.avenzu.azura.check.checks.combat.velocity.VelocityA;
import me.avenzu.azura.check.checks.misc.PingSpoof;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsA;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsB;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsC;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsD;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsE;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsF;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsG;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsH;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsI;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsJ;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsK;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsL;
import me.avenzu.azura.check.checks.misc.badpackets.BadPacketsM;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksA;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksB;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksC;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksD;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksE;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksF;
import me.avenzu.azura.check.checks.misc.invtweaks.InvTweaksG;
import me.avenzu.azura.check.checks.misc.timer.Timer;
import me.avenzu.azura.check.checks.misc.timer.TimerB;
import me.avenzu.azura.check.checks.motion.OmniSprint;
import me.avenzu.azura.check.checks.motion.fly.FlyA;
import me.avenzu.azura.check.checks.motion.scaffold.ScaffoldA;
import me.avenzu.azura.check.checks.motion.speed.SpeedA;
import me.avenzu.azura.check.checks.motion.speed.SpeedB;
import me.avenzu.azura.stats.PlayerStats;

@RequiredArgsConstructor
public class CheckManager {

    private final PlayerStats playerStats;

    @Getter
    private final Deque<Check> checks = new LinkedList<>();

    public void start() {
        addCheck(new AimAssistA(playerStats));

        addCheck(new AutoClickerA(playerStats));
        
        addCheck(new ReachA(playerStats));

        addCheck(new AuraA(playerStats));
        addCheck(new AuraB(playerStats));

        addCheck(new VelocityA(playerStats));

        addCheck(new BadPacketsA(playerStats));
        addCheck(new BadPacketsB(playerStats));
        addCheck(new BadPacketsC(playerStats));
        addCheck(new BadPacketsD(playerStats));
        addCheck(new BadPacketsE(playerStats));
        addCheck(new BadPacketsF(playerStats));
        addCheck(new BadPacketsG(playerStats));
        addCheck(new BadPacketsH(playerStats));
        addCheck(new BadPacketsI(playerStats));
        addCheck(new BadPacketsJ(playerStats));
        addCheck(new BadPacketsK(playerStats));
        addCheck(new BadPacketsL(playerStats));
        addCheck(new BadPacketsM(playerStats));

        addCheck(new InvTweaksA(playerStats));
        addCheck(new InvTweaksB(playerStats));
        addCheck(new InvTweaksC(playerStats));
        addCheck(new InvTweaksD(playerStats));
        addCheck(new InvTweaksE(playerStats));
        addCheck(new InvTweaksF(playerStats));
        addCheck(new InvTweaksG(playerStats));

        addCheck(new Timer(playerStats));
        addCheck(new TimerB(playerStats));
        
        addCheck(new PingSpoof(playerStats));
        
        addCheck(new SpeedA(playerStats));
        addCheck(new SpeedB(playerStats));

        addCheck(new FlyA(playerStats));
        
        addCheck(new OmniSprint(playerStats));
        
        addCheck(new ScaffoldA(playerStats));
    }

    private void addCheck(Check check) {
        checks.addLast(check);
    }
}
