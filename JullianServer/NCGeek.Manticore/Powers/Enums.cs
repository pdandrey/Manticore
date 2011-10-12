using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NCGeek.Manticore.Powers
{
    public enum PowerUsage
    {
        AtWill,
        Encounter,
        Daily
    }

    public enum ActionType
    {
        FreeAction,
        ImmediateInterrupt,
        ImmediateReaction,
        MinorAction,
        MoveAction,
        NoAction,
        OpportunityAction,
        StandardAction
    }

    public enum Keywords
    {
        Acid,
        Arcane,
        Augmentable,
        Aura,
        Beast,
        BeastForm,
        Bladespell,
        ChannelDivinity,
        Charm,
        Cold,
        Conjuration,
        Divine,
        Elemental,
        Enchantment,
        Evocation,
        Fear,
        Fire,
        Force,
        FullDiscipline,
        Healing,
        Illusion,
        Implement,
        Invigorating,
        Lightning,
        Martial,
        Necromancy,
        Necrotic,
        Nethermancy,
        Poison,
        Polymorph,
        Primal,
        Psionic,
        Psychic,
        Radiant,
        Rage,
        Rattling,
        Reliable,
        Runic,
        Shadow,
        Sleep,
        Spirit,
        Stance,
        Summoning,
        Teleportation,
        Thunder,
        Transmutation,
        Varies,
        Weapon,
        Zone
    }

    public enum AttackTypeType
    {
        Area,
        Close,
        Melee,
        Ranged,
        Personal,
        Special
    }

    public enum AttackTypeShape
    {
        Burst,
        Blast,
        Wall,
        Touch,
        Weapon,
        Reach
    }

    public enum AttackTypeTarget
    {
        Self,
        Ally,
        Spirit,
        Beast,
        Familiar
    }
}
