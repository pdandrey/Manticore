using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JullianServer.Models;
using System.Security.Cryptography;
using System.Text;
using System.IO;
using System.Text.RegularExpressions;

namespace JullianServer.Controllers
{
    public class PartyController : Controller
    {
        private static readonly MD5 _hash = MD5.Create();

        private Dictionary<string, Character> _party;
        private Dictionary<string, Character> Party
        {
            get
            {
                if (_party == null)
                {
                    _party = HttpContext.Application["Party"] as Dictionary<string, Character>;
                    if (_party == null)
                    {
                        _party = new Dictionary<string, Character>();
                        HttpContext.Application["Party"] = _party;
                    }
                }
                return _party;
            }
        }

        private Dictionary<Guid, Character> _partyByGuid;
        private Dictionary<Guid, Character> PartyByGuid
        {
            get
            {
                if (_partyByGuid == null)
                    _partyByGuid = Party.Values.ToDictionary(c => c.ID, c => c);
                return _partyByGuid;
            }
        }

        //
        // GET: /Party/
        public ActionResult Join(Character character)
        {
            string toHash = character.Name;
            character.Token = Convert.ToBase64String(_hash.ComputeHash(Encoding.UTF8.GetBytes(toHash)));

            if (Party.ContainsKey(character.Token))
            {
                return Json(new PartyJoinedEvent { Success = true, Message="Character is already in the party", ExistingMember=true, ID=character.ID, Token = character.Token, PartyMembers = Party.Values.Where(p => !p.Token.Equals(character.Token)).ToArray()});
            }

            CacheImage(character.Portrait);

            BroadcastEvent(new CharacterJoinedEvent { Character = character });
            var party = Party.Values.ToArray();
            Party[character.Token] = character;
            return Json(new PartyJoinedEvent { Success = true, Message = "Party joined", ExistingMember=false, ID=character.ID, Token = character.Token, PartyMembers = party });
        }

        private void CacheImage(string url)
        {
            if (string.IsNullOrWhiteSpace(url))
                return;

            string cacheDir = Server.MapPath("~/Cache");
            if (!Directory.Exists(cacheDir))
                Directory.CreateDirectory(cacheDir);

            if (!cacheDir.EndsWith("\\"))
                cacheDir += '\\';
            
            //string hash = Convert.ToBase64String(_hash.ComputeHash(Encoding.UTF8.GetBytes(url)));
            //string extension = url.Substring(url.LastIndexOf('.'));
            //string file = hash + extension;

            Regex r = new Regex(@"\w+\.\w+$");
            Match m = r.Match(url);
            if (!m.Success)
                return;

            string file = m.Value;

            if (!System.IO.File.Exists(file))
            {
                int i = 0;
            }
        }

        public JsonResult Chat(string token, string message)
        {
            if (!Party.ContainsKey(token))
            {
                return Json(new Event { Success = false, Message = "Character is not in the party" });
            }

            var ch = Party[token];
            PartyChatEvent evt = new PartyChatEvent { Message = message, From = ch };
            BroadcastEvent(evt, ch);

            return Json(new Event { Success = true, Message = "Chat sent" });
        }
        
        public JsonResult Leave(string token)
        {
            if (!Party.ContainsKey(token))
            {
                return Json(new Event { Success = false, Message = "Character is not in the party" });
            }

            var ch = Party[token];
            Party.Remove(token);
            BroadcastEvent(new CharacterLeftEvent() { Character = ch });
            return Json(new Event { Success = true, Message = "Character is no longer in the party" });
        }

        public JsonResult Message(string from, Guid to, string message)
        {
            Character chFrom = Party[from];
            Character chTo = PartyByGuid[to];
            chTo.EventQueue.Enqueue(new MessageEvent { From = chFrom, Message = message });
            return Json(new Event { Success = true, Message = "Message sent to " + chTo.Name });
        }

        public JsonResult Update(string token, int hp, int tempHP, int surges, int deathSaves)
        {
            Character ch = Party[token];

            if (hp > ch.MaxHP)
                return Json(new Event { Success = false, Message = "Invalid HP value: " + hp });

            if(tempHP < 0)
                return Json(new Event { Success = false, Message = "Invalid TempHP value: " + tempHP });

            if(surges < 0 || surges > ch.MaxSurges)
                return Json(new Event { Success = false, Message = "Invalid surge value: " + surges });

            if(deathSaves < 0 || deathSaves > 3)
                return Json(new Event { Success = false, Message = "Invalid death save value: " + deathSaves });

            ch.CurrentHP = hp;
            ch.TempHP = tempHP;
            ch.CurrentSurges = surges;
            ch.DeathSaves = deathSaves;
            BroadcastEvent(new CharacterUpdateEvent(ch), ch);

            return Json(new Event { Success = true, Message = "Character updated" });
        }

        public JsonResult GetEvents(string token)
        {
            Character ch = Party[token];
            var queue = ch.EventQueue.ToArray();
            ch.EventQueue.Clear();
            return Json(new GetEventsEvent { Success = true, Message = "events", Events = queue });
        }

        public ActionResult Index()
        {
            return View(Party.Values.ToList());
        }

        public PartialViewResult PartyInfo()
        {
            return PartialView(Party.Values.ToList());
        }

        private void BroadcastEvent(Event evt, params Character[] skip) {
            var chars = Party.Values.AsEnumerable();

            if (skip != null && skip.Length > 0)
            {
                chars = chars.Where(c => !skip.Contains(c));
            }

            foreach(Character c in chars) {
                c.EventQueue.Enqueue(evt);
            }
        }
    }
}
