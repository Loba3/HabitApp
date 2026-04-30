namespace HabitTrackerAPI.Models
{
    public class Habit
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }

        public List<HabitLog>? Logs { get; set; }
    }
}