package characters;

import abilities.doctorstrange.*;

public class DoctorStrange extends Hero {

    public DoctorStrange() {
        super("Doctor Strange", 2100, 18, 11, 650);
        setSkill1(new MysticGuard());
        setSkill2(new TimeRestore());
        setSkill3(new IvannaAstralJudgment());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/doctor_strange.png");
    }

    @Override
    public String getDescription() {
        return "Protector of the multiverse who understands the danger of " +
                "cloning technology and time manipulation. He can create shields, " +
                "heal allies, and cast Astral Judgment that ignores all defense.";
    }
}