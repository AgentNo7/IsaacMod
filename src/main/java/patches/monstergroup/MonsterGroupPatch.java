package patches.monstergroup;

public class MonsterGroupPatch {

//    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.MonsterGroup", method = "update")
//    public static class UpdateInsertPatch
//    {
//        @SpireInsertPatch(rloc = 4)
//        public static void Insert(final MonsterGroup group) {
//            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH) {
//                group.hoveredMonster = null;
//                for (final AbstractMonster m : MinionHelper.getMinionMonsters()) {
//                    if (!m.isDying && !m.isEscaping) {
//                        m.hb.update();
//                        m.intentHb.update();
//                        m.healthHb.update();
//                        if ((m.hb.hovered || m.intentHb.hovered || m.healthHb.hovered) && !AbstractDungeon.player.isDraggingCard) {
//                            group.hoveredMonster = m;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @SpirePatch(cls = "com.megacrit.cardcrawl.monsters.MonsterGroup", method = "unhover")
//    public static class UnhoverPatch
//    {
//        @SpirePostfixPatch
//        public static void Postfix(final MonsterGroup group) {
//            for (final AbstractMonster m : MinionHelper.getMinionMonsters()) {
//                m.unhover();
//            }
//        }
//    }
}
