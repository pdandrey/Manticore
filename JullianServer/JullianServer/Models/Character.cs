using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Script.Serialization;
using System.Text.RegularExpressions;

namespace JullianServer.Models
{
    public class Character
    {
        public static readonly Regex PORTRAIT_REGEX = new Regex(@"\w+\.\w+$");

        [ScriptIgnore]
        public string Token { get; set; }
        public Guid ID { get; private set; }
        public string Name { get; set; }
        public int MaxHP { get; set; }
        public int CurrentHP { get; set; }
        public int TempHP { get; set; }
        public int DeathSaves { get; set; }
        public int MaxSurges { get; set; }
        public int CurrentSurges { get; set; }
        public string Portrait { get; set; }

        [ScriptIgnore]
        public string CachedPortrait
        {
            get
            {
                Match m = PORTRAIT_REGEX.Match(Portrait);
                if (m.Success)
                    return "Cache/" + m.Value;
                else
                    return null;
            }
        }

        [ScriptIgnore]
        public Queue<Event> EventQueue { get; private set; }

        public Character()
        {
            EventQueue = new Queue<Event>();
            ID = Guid.NewGuid();
        }
    }

    public enum PartyEventType
    {
        GenericEvent,
        PartyJoined,
        CharacterJoined,
        CharacterLeft,
        Message,
        CharacterUpdate,
        PartyChat,
        GetEvents
    }
    public class Event {
        public bool Success { get; set; }
        public string Message { get; set; }
        [ScriptIgnore]
        public DateTimeOffset CreatedOn { get; private set; }
        [ScriptIgnore]
        public PartyEventType EventType { get; private set; }
        public string Timestamp
        {
            get { return CreatedOn.ToString("u"); }
        }
        public string EventName
        {
            get
            {
                return EventType.ToString();
            }
            set { }
        }

        public Event() : this(PartyEventType.GenericEvent) { }
        protected Event(PartyEventType type)
        {
            EventType = type;
            CreatedOn = DateTimeOffset.Now;
        }
    }

    public class GetEventsEvent : Event
    {
        public Event[] Events { get; set; }
        public GetEventsEvent() : base(PartyEventType.GetEvents) { }
    }

    public class PartyJoinedEvent : Event
    {
        public Character[] PartyMembers { get; set; }
        public string Token { get; set; }
        public Guid ID { get; set; }
        public bool ExistingMember { get; set; }
        public PartyJoinedEvent() : base(PartyEventType.PartyJoined) { }
        public override string ToString()
        {
            return "Party joined.";
        }
    }

    public class CharacterJoinedEvent : Event
    {
        public Character Character { get; set; }
        public CharacterJoinedEvent() : base(PartyEventType.CharacterJoined) {}
        public override string ToString()
        {
            return string.Format("{0} joined the party", Character.Name);
        }
    }
    public class CharacterLeftEvent : Event
    {
        [ScriptIgnore]
        public Character Character { get; set; }
        public Guid CharacterID
        {
            get
            {
                return Character.ID;
            }
            set { }
        }
        public CharacterLeftEvent() : base(PartyEventType.CharacterLeft) { }
        public override string ToString()
        {
            return string.Format("{0} left the party", Character.Name);
        }
    }
    public class PartyChatEvent : Event
    {
        [ScriptIgnore]
        public Character From { get; set; }
        public Guid FromID { get { return From.ID; } }
        public PartyChatEvent() : base(PartyEventType.PartyChat) { }
        public override string ToString()
        {
            return string.Format("Party chat from {0}: {1}", From.Name, Message);
        }
    }
    public class MessageEvent : Event
    {
        [ScriptIgnore]
        public Character From { get; set; }
        public Guid FromID
        {
            get
            {
                return From.ID;
            }
            set { }
        }
        public MessageEvent() : base(PartyEventType.Message) { }
        public override string ToString()
        {
            return string.Format("Message From {0}: {1}", From.Name, Message);
        }
    }
    public class CharacterUpdateEvent : Event
    {
        public Guid ID { get; private set; }
        [ScriptIgnore]
        public string Name { get; private set; }
        public int HP { get; private set; }
        public int TempHP { get; private set; }
        public int Surges { get; private set; }
        public int DeathSaves { get; private set; }

        public CharacterUpdateEvent(Character ch) : base(PartyEventType.CharacterUpdate) 
        {
            ID = ch.ID;
            Name = ch.Name;
            HP = ch.CurrentHP;
            TempHP = ch.TempHP;
            Surges = ch.CurrentSurges;
            DeathSaves = ch.DeathSaves;
        }
        public override string ToString()
        {
            return string.Format("{0} Updated: {1} hp, {2} tempHP, {3} surges, {4} deathSaves",
                Name,
                HP,
                TempHP,
                Surges,
                DeathSaves);
        }
    }
}