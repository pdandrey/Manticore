using System;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Data;
using System.Data.OleDb;
using NCGeek.Manticore.Powers;
using System.Xml.Linq;

namespace NCGeek.Manticore.Test
{
    /// <summary>
    /// Summary description for UnitTest1
    /// </summary>
    [TestClass]
    [DeploymentItem(@"NCGeek.Manticore.Test\Data\AttackTypes.xlsx")]
    public class AttackTypeTest
    {
        public AttackTypeTest()
        {
            //
            // TODO: Add constructor logic here
            //
        }

        private TestContext testContextInstance;

        /// <summary>
        ///Gets or sets the test context which provides
        ///information about and functionality for the current test run.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }

        #region Additional test attributes
        //
        // You can use the following additional attributes as you write your tests:
        //
        // Use ClassInitialize to run code before running the first test in the class
        // [ClassInitialize()]
        // public static void MyClassInitialize(TestContext testContext) { }
        //
        // Use ClassCleanup to run code after all tests in a class have run
        // [ClassCleanup()]
        // public static void MyClassCleanup() { }
        //
        // Use TestInitialize to run code before running each test 
        // [TestInitialize()]
        // public void MyTestInitialize() { }
        //
        // Use TestCleanup to run code after each test has run
        // [TestCleanup()]
        // public void MyTestCleanup() { }
        //
        #endregion

        [TestMethod]
        [DataSource("System.Data.OleDb", @"Provider=Microsoft.ACE.OLEDB.12.0;Data Source=|DataDirectory|\AttackTypes.xlsx;Extended Properties='Excel 8.0';", "Area$", DataAccessMethod.Sequential)]
        public void TestArea()
        {
            string input = GetString("Input");
            AttackType at = PowerParser.ParseAttackType(new XElement("specific", new XAttribute("name", "Attack Type"), input));

            Assert.AreEqual<AttackTypeType>(AttackTypeType.Area, at.Type, "Type");

            AttackTypeShape shape = AttackTypeShape.Reach;
            if(!Enum.TryParse<AttackTypeShape>(GetString("Shape"), true, out shape))
                Assert.Fail("Could not convert shape " + GetString("Shape"));

            Assert.AreEqual<AttackTypeShape>(shape, at.Shape, "Shape");

            HashSet<AttackTypeTarget> targets = new HashSet<AttackTypeTarget>();
            string strTarget = GetString("Target");
            if(!string.IsNullOrWhiteSpace(strTarget))
            {
                foreach(string str in strTarget.Split(','))
                {
                    AttackTypeTarget tmp = AttackTypeTarget.Beast;
                    if(!Enum.TryParse<AttackTypeTarget>(str.Trim(), true, out tmp))
                    {
                        Assert.Fail("Could not parse target of " + strTarget);
                    }
                    else
                    {
                        targets.Add(tmp);
                    }
                }
            }

            foreach(AttackTypeTarget att in targets)
            {
                Assert.IsTrue(at.Targets.Contains(att), "Target {0}", att);
            }

            IRange size = null;
            string strSize = GetString("Size");
            if(!string.IsNullOrWhiteSpace(strSize))
            {
                int i = -1;
                if(int.TryParse(strSize, out i))
                {
                    size = new IntegerRange { Distance = i };
                }
                else
                {
                    size = new DiceRange { Distance = strSize };
                }
                Assert.AreEqual<IRange>(size, at.Range[0], "Size");
            }
            else
            {
                Assert.AreEqual<int>(0, at.Range.Count, "Empty Range");
            }


            IRange distance = null;
            string strDistance = GetString("Distance");
            if(string.IsNullOrWhiteSpace(strDistance))
            {
                Assert.IsTrue(at.Range.Count < 2, "No distance");
            }
            else
            {
                int i = -1;
                if(int.TryParse(strDistance, out i))
                {
                    distance = new IntegerRange { Distance = i };
                }
                else
                {
                    distance = new StringRange { Distance = strDistance };
                }
                Assert.AreEqual<int>(2, at.Range.Count, "Range Count for Distance");
                Assert.AreEqual<IRange>(distance, at.Range[1], "Distance");
            }

            Assert.AreEqual<string>(GetString("Centered"), at.CenteredOn, "Centered");

            Assert.AreEqual<bool>((bool)TestContext.DataRow["Special"], at.IsSpecial, "Special");
        }

        private string GetString(string columnName)
        {
            object o = TestContext.DataRow[columnName];
            if(o is DBNull)
                return null;
            return o.ToString();
        }
    }

 
}
