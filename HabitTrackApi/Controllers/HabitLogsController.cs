using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using HabitTrackerAPI.Data;
using HabitTrackerAPI.Models;

namespace HabitTrackerAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HabitLogsController : ControllerBase
    {
        private readonly AppDbContext _context;

        public HabitLogsController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<HabitLog>>> GetLogs()
        {
            return await _context.HabitLogs.ToListAsync();
        }

        [HttpPost]
        public async Task<ActionResult<HabitLog>> CreateLog(HabitLog log)
        {
            // Validate that the habit exists
            var habitExists = await _context.Habits.AnyAsync(h => h.Id == log.HabitId);
            if (!habitExists)
            {
                return BadRequest($"Habit with ID {log.HabitId} does not exist.");
            }

            // Ensure we only use the HabitId, not the full Habit object
            log.Habit = null;

            _context.HabitLogs.Add(log);
            await _context.SaveChangesAsync();
            return CreatedAtAction(nameof(GetLogs), new { id = log.Id }, log);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateLog(int id, HabitLog updatedLog)
        {
            var log = await _context.HabitLogs.FindAsync(id);
            if (log == null) return NotFound();

            log.Completed = updatedLog.Completed;
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}