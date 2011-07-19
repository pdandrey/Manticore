using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace JullianServer.Models
{
    public class BarModel
    {
        public static BarModel HPBarModel(Character ch)
        {
            string temp = string.Empty;
            if(ch.TempHP > 0)
                temp = string.Format("+{0}", ch.TempHP);

            BarModel model = new BarModel
            {
                Max = ch.MaxHP,
                Min = 0,
                Current = Math.Max(0, ch.CurrentHP),
                Class = "hp",
                Label = string.Format("HP: {0}{1}/{2}", ch.CurrentHP, temp, ch.MaxHP)
            };
            model.AddConditionalClass(50, "Bloodied");
            return model;
        }

        public static BarModel SurgeBarModel(Character ch)
        {
            BarModel model = new BarModel
            {
                Max = ch.MaxSurges,
                Min = 0,
                Current = ch.CurrentSurges,
                Class = "surges",
                Label = string.Format("Surges: {0}/{1}", ch.CurrentSurges, ch.MaxSurges)
            };

            return model;
        }

        public int Max { get; set; }
        public int Min { get; set; }
        public int Current { get; set; }
        public string Class { get; set; }
        public string Label { get; set; }

        public int DataValue
        {
            get
            {
                return (int)((double)Current / (double)Max * 100);
            }
        }

        public string ConditionalClass
        {
            get
            {
                int value = DataValue;
                var classKey = (from key in conditionalClasses.Keys
                                where value < key
                                orderby key
                                select key);

                if (classKey.Count() == 0)
                    return string.Empty;
                else
                    return conditionalClasses[classKey.Min()];
            }
        }

        private Dictionary<int, string> conditionalClasses;

        public BarModel()
        {
            conditionalClasses = new Dictionary<int, string>();
        }

        public void AddConditionalClass(int percent, string className)
        {
            conditionalClasses.Add(percent, className);
        }
    }
}