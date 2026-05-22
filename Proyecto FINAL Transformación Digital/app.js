// ── Charts ──────────────────────────────────────────────────────────────────

const chartDefaults = {
  color: '#8899aa',
  font: { family: 'Inter', size: 12 }
};
Chart.defaults.color = chartDefaults.color;
Chart.defaults.font.family = chartDefaults.font.family;

// MTTR Chart
const mttrCtx = document.getElementById('mttrChart');
if (mttrCtx) {
  new Chart(mttrCtx, {
    type: 'line',
    data: {
      labels: ['Mes 1','Mes 2','Mes 3','Mes 4','Mes 5','Mes 6','Mes 7','Mes 8','Mes 9','Mes 10','Mes 11','Mes 12'],
      datasets: [{
        label: 'MTTR (horas)',
        data: [4.2, 4.0, 3.8, 3.4, 3.0, 2.6, 2.3, 2.0, 1.9, 1.8, 1.7, 1.7],
        borderColor: '#00d4ff',
        backgroundColor: 'rgba(0,212,255,0.1)',
        borderWidth: 2.5,
        pointBackgroundColor: '#00d4ff',
        pointRadius: 4,
        tension: 0.4,
        fill: true
      }]
    },
    options: {
      responsive: true,
      plugins: { legend: { display: false } },
      scales: {
        x: { grid: { color: 'rgba(255,255,255,0.05)' } },
        y: {
          grid: { color: 'rgba(255,255,255,0.05)' },
          min: 0, max: 5,
          ticks: { callback: v => v + 'h' }
        }
      }
    }
  });
}

// Alarm Suppression Chart
const alarmCtx = document.getElementById('alarmChart');
if (alarmCtx) {
  new Chart(alarmCtx, {
    type: 'doughnut',
    data: {
      labels: ['Alarmas suprimidas (ruido)', 'Alarmas accionables'],
      datasets: [{
        data: [85, 15],
        backgroundColor: ['rgba(124,58,237,0.7)', 'rgba(0,212,255,0.8)'],
        borderColor: ['#7c3aed', '#00d4ff'],
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      cutout: '70%',
      plugins: {
        legend: { position: 'bottom', labels: { padding: 20, font: { size: 12 } } },
        tooltip: { callbacks: { label: ctx => ` ${ctx.label}: ${ctx.parsed}%` } }
      }
    }
  });
}

// ── Navbar active state on scroll ───────────────────────────────────────────
const sections = document.querySelectorAll('section[id]');
const navLinks = document.querySelectorAll('.nav-links a');

const observer = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      navLinks.forEach(link => {
        link.style.color = link.getAttribute('href') === '#' + entry.target.id
          ? 'var(--accent)' : '';
      });
    }
  });
}, { threshold: 0.3 });

sections.forEach(s => observer.observe(s));

// ── Animate elements on scroll ──────────────────────────────────────────────
const animObserver = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.style.opacity = '1';
      entry.target.style.transform = 'translateY(0)';
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.card, .value-card, .ml-card, .tl-item, .level, .workflow-step').forEach(el => {
  el.style.opacity = '0';
  el.style.transform = 'translateY(20px)';
  el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
  animObserver.observe(el);
});

// ── Typewriter for hero ──────────────────────────────────────────────────────
const heroSub = document.querySelector('.hero-sub');
if (heroSub) {
  const text = heroSub.innerHTML;
  heroSub.innerHTML = '';
  let i = 0;
  const interval = setInterval(() => {
    heroSub.innerHTML = text.substring(0, i);
    i++;
    if (i > text.length) clearInterval(interval);
  }, 12);
}
