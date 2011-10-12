using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NCGeek.Manticore.Powers
{
    public class AttackType
    {
        public AttackTypeType Type { get; set; }

        public AttackTypeShape Shape { get; set; }

        public HashSet<AttackTypeTarget> Targets { get; set; }

        public List<IRange> Range { get; set; }

        public string CenteredOn { get; set; }

        public bool IsSpecial { get; set; }

        public AttackType()
        {
            Targets = new HashSet<AttackTypeTarget>();
            Range = new List<IRange>();
            IsSpecial = false;
        }
    }
}
