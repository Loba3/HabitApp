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
            _context.HabitLogs.Add(log);
            await _context.SaveChangesAsync();
            return log;
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