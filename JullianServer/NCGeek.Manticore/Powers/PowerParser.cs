using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Linq;
using System.Text.RegularExpressions;

namespace NCGeek.Manticore.Powers
{
    public static class PowerParser
    {
        public static Power ParsePower(XElement xml)
        {

            return null;
        }

        public static AttackType ParseAttackType(XElement xml)
        {
            Regex regexArea = new Regex(@"^Area(?:\s+(?<shape>burst|wall))?(?:\s+(?<spirit>spirit))?(?:\s+(?<special>special))?(?:\s+(?<size>[d\d]+))?(?:\s+centered o[nf] (?<centered>.*?))?(?:\s+square)?(?:\s+with(?:in)?\s+(?<range>\d+|weapon(?:'s)? range)(?:\s+squares?)?)?(?:\s+and)?(?:\s+centered on (?<centered>.*?))?(?:\s*of you)?$");
		    Regex regexClose = new Regex(@"^Close(?:(?: (?<type>blast|burst|touch|wall))?(?: (?<spirit>spirit))?(?: (?<size>[d\d]+))?(?: \((?:(?:increase to close burst (?<lvlRange>\d+) at (?<lvl>\d+)(?:st|nd|rd|th) level)|(?<beast>beast)|(?:(?<lvlRange>\d+) at (?<lvl>\d+)(?:st|nd|rd|th) level(?:[,\s]*))+)\))?(?:,? centered on (?<centered>.*?)(?: within (?<centerRange>\d+) squares)?)?(?: or)?)+$");
            Regex regexOther = new Regex(@"^(?<choice>(?<type>Melee|Personal|Ranged|Special)(?: (?<beast>beast(?: form)?|spirit?))?(?: (?<reach>reach))?\s?(?<range1>\d*|touch|weapon|weaopn|weapoon)(?:/(?<range2>\d+))?(?: (?<distance>miles?|sight))?(?: or )?(?: \((?<extra>.*?)\))?(?: (?<reach>\+\s?\d(?: reach)?))?)+$");

            if(!xml.Name.LocalName.Equals("specific") && !xml.Attribute("name").Value.Equals("Attack Type"))
                return null;

            Match m = regexArea.Match(xml.Value.Trim());
            if(m.Success)
                return ParseAreaAttack(m);

            m = regexClose.Match(xml.Value.Trim());
            if(m.Success)
                return ParseCloseAttack(m);

            m = regexOther.Match(xml.Value.Trim());
            if(m.Success)
                return ParseOtherAttack(m);

            return null;
        }

        private static AttackType ParseCloseAttack(Match m)
        {
            throw new NotImplementedException();
        }

        private static AttackType ParseOtherAttack(Match m)
        {
            throw new NotImplementedException();
        }

        private static AttackType ParseAreaAttack(Match m)
        {
            AttackType ret = new AttackType()
            {
                 Type = AttackTypeType.Area
            };

            //new Regex(@"^Area(?:\s+(?<shape>burst|wall))?(?:\s+(?<spirit>spirit))?(?:\s+(?<special>special))?(?:\s+(?<size>[d\d]+))?(?:\s+centered o[nf] (?<centered>.*?))?(?:\s+square)?(?:\s+with(?:in)?\s+(?<range>\d+|weapon(?:'s)? range)(?:\s+squares?)?)?(?:\s+and)?(?:\s+centered on (?<centered>.*?))?$");
            AttackTypeShape shape = AttackTypeShape.Burst;
            if(m.Groups["shape"].Success)
            {
                if(!Enum.TryParse<AttackTypeShape>(m.Groups["shape"].Value, true, out shape))
                {
                    throw new Exception("Unknown area shape: " + m.Groups["shape"].Value);
                }
            }
            ret.Shape = shape;

            

            ret.IsSpecial = m.Groups["special"].Success;

            if(m.Groups["size"].Success)
            {
                string size = m.Groups["size"].Value;
                int iSize = -1;
                if(int.TryParse(size, out iSize))
                    ret.Range.Add(new IntegerRange { Distance = iSize });
                else
                    ret.Range.Add(new DiceRange { Distance = size });
            }

            if(m.Groups["centered"].Success)
            {
                ret.CenteredOn = m.Groups["centered"].Value.Trim();
                if(ret.CenteredOn.ToLower().Contains("ally") || ret.CenteredOn.ToLower().Contains("allies"))
                    ret.Targets.Add(AttackTypeTarget.Ally);
                if(ret.CenteredOn.ToLower().Contains("familiar"))
                    ret.Targets.Add(AttackTypeTarget.Familiar);
                if(ret.CenteredOn.ToLower().Contains("spirit"))
                    ret.Targets.Add(AttackTypeTarget.Spirit);
                if(ret.CenteredOn.ToLower().Contains("you "))
                    ret.Targets.Add(AttackTypeTarget.Self);
            }

            if(m.Groups["spirit"].Success)
            {
                ret.Targets.Add(AttackTypeTarget.Spirit);
                ret.CenteredOn = "spirit";
            }

            if(m.Groups["range"].Success)
            {
                int range = -1;
                if(int.TryParse(m.Groups["range"].Value.Trim(), out range))
                {
                    ret.Range.Add(new IntegerRange { Distance = range });
                }
                else
                {
                    ret.Range.Add(new StringRange { Distance = m.Groups["range"].Value.Trim() });
                }
            }

            return ret;
        }
    }
}
