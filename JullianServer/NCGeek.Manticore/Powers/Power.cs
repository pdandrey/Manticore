using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NCGeek.Manticore.Powers
{
    public class Power
    {
        public int ID { get; set; }
        public PowerUsage Usage { get; set; }
        public bool IsSpecial { get; set; }
        public string Display { get; set; }
        public ActionType Action { get; set; }
        public HashSet<Keywords> Keywords { get; set; }
        public HashSet<Keywords> AlternateKeywords { get; set; }
    }
}
