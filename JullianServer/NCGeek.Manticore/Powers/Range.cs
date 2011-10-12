using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NCGeek.Manticore.Powers
{
    public interface IRange 
    { }

    public class IntegerRange : IRange
    {
        public int Distance { get; set; }
        public override string ToString()
        {
            return Distance.ToString();
        }
        public override bool Equals(object obj)
        {
            if(obj is IntegerRange)
            {
                IntegerRange ir = (IntegerRange)obj;
                return Distance == ir.Distance;
            }
            return false;
        }
    }

    public class DiceRange : IRange
    {
        public string Distance { get; set; }
        public override string ToString()
        {
            return Distance;
        }
        public override bool Equals(object obj)
        {
            DiceRange dr = obj as DiceRange;
            return dr != null && dr.Distance.Equals(Distance);
        }
    }

    public class StringRange : IRange
    {
        public string Distance { get; set; }
        public override string ToString()
        {
            return Distance;
        }
        public override bool Equals(object obj)
        {
            StringRange sr = obj as StringRange;
            return sr != null && string.Equals(Distance, sr.Distance);
        }
    }
}
