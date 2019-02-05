package patches.action;

public class EscapeActionPatch {

//    @SpirePatch(cls = "com.megacrit.cardcrawl.actions.common.EscapeAction", method = "update")
//    public static class UseCardActionPatch {
//        @SpireInsertPatch(loc = 17)
//        public static SpireReturn Insert(EscapeAction action) {
//            if (action.source instanceof AbstractMonster && ((AbstractMonster) action.source).type == AbstractMonster.EnemyType.BOSS) {
//                try {
//                    Method tickDuration = AbstractGameAction.class.getDeclaredMethod("tickDuration");
//                    tickDuration.setAccessible(true);
//                    tickDuration.invoke(action);
//                    return SpireReturn.Return(null);
//                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }
}
