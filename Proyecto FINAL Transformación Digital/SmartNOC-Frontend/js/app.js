const API = 'http://localhost:8080/api/v1';

// ── Estado de conexión ────────────────────────────────────────────────────────
function setApiStatus(ok) {
  const dot  = document.getElementById('api-dot');
  const text = document.getElementById('api-text');
  if (!dot) return;
  if (ok) {
    dot.style.background  = '#10b981';
    dot.style.boxShadow   = '0 0 8px #10b981';
    text.textContent      = '✅ API conectada — Spring Boot + MySQL activos en localhost:8080';
    text.style.color      = '#10b981';
  } else {
    dot.style.background  = '#ef4444';
    text.textContent      = '❌ Sin conexión con la API. ¿Está arrancado el backend en Eclipse?';
    text.style.color      = '#ef4444';
  }
}

// ── Cargar todos los datos del dashboard ─────────────────────────────────────
async function cargarDatos() {
  try {
    // KPIs
    const kpiRes = await fetch(`${API}/incidencias/kpis`);
    if (!kpiRes.ok) throw new Error('API no disponible');
    const kpis = await kpiRes.json();
    document.getElementById('kpi-mttr').textContent    = kpis.mttrPromedioMinutos || '0';
    document.getElementById('kpi-activas').textContent = kpis.incidenciasActivas  || '0';
    setApiStatus(true);

    // Incidencias
    const incRes  = await fetch(`${API}/incidencias`);
    const incList = await incRes.json();
    document.getElementById('kpi-total').textContent = incList.length;
    renderIncidencias(incList);

    // Técnicos
    const tecRes  = await fetch(`${API}/tecnicos`);
    const tecList = await tecRes.json();
    const disponibles = tecList.filter(t => t.disponible).length;
    document.getElementById('kpi-tecnicos').textContent = disponibles;
    renderTecnicos(tecList);

  } catch (e) {
    setApiStatus(false);
    ['kpi-mttr','kpi-activas','kpi-total','kpi-tecnicos'].forEach(id => {
      const el = document.getElementById(id);
      if (el) el.textContent = '--';
    });
    const bodyInc = document.getElementById('body-incidencias');
    if (bodyInc) bodyInc.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#ef4444">Backend no disponible. Arranca SmartNOCApplication en Eclipse.</td></tr>';
    const bodyTec = document.getElementById('body-tecnicos');
    if (bodyTec) bodyTec.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#ef4444">Backend no disponible.</td></tr>';
  }
}

// ── Renderizar tabla de incidencias ──────────────────────────────────────────
function renderIncidencias(list) {
  const tbody = document.getElementById('body-incidencias');
  if (!tbody) return;
  if (list.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:var(--muted)">No hay incidencias registradas.</td></tr>';
    return;
  }
  const colors = { P1: '#ef4444', P2: '#f97316', P3: '#facc15', P4: '#22c55e' };
  const estadoColors = { ABIERTA: '#ef4444', EN_PROGRESO: '#f97316', RESUELTA: '#22c55e', CERRADA: '#64748b' };
  tbody.innerHTML = list.map(i => {
    const color  = colors[i.severidad]      || '#aaa';
    const eColor = estadoColors[i.estado]   || '#aaa';
    const fecha  = i.fechaApertura ? new Date(i.fechaApertura).toLocaleString('es-ES') : '—';
    return `<tr>
      <td style="font-family:monospace;font-size:0.8rem">${i.codigo || '—'}</td>
      <td>${i.titulo}</td>
      <td><span style="color:${color};font-weight:700">${i.severidad}</span></td>
      <td><span style="color:${eColor}">${i.estado}</span></td>
      <td style="text-align:center">${i.clientesAfectados ?? 0}</td>
      <td style="font-size:0.8rem;color:var(--muted)">${fecha}</td>
    </tr>`;
  }).join('');
}

// ── Renderizar tabla de técnicos ─────────────────────────────────────────────
function renderTecnicos(list) {
  const tbody = document.getElementById('body-tecnicos');
  if (!tbody) return;
  if (list.length === 0) {
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:var(--muted)">No hay técnicos registrados.</td></tr>';
    return;
  }
  tbody.innerHTML = list.map(t => `<tr>
    <td><strong>${t.nombre} ${t.apellidos}</strong></td>
    <td style="color:var(--muted);font-size:0.8rem">${t.email}</td>
    <td style="font-size:0.8rem">${t.skills || '—'}</td>
    <td>${t.zona || '—'}</td>
    <td>${t.disponible ? '<span style="color:#10b981;font-weight:700">✅ Sí</span>' : '<span style="color:#ef4444">🔴 No</span>'}</td>
  </tr>`).join('');
}

// ── Crear nueva incidencia (POST) ────────────────────────────────────────────
async function crearIncidencia(e) {
  e.preventDefault();
  const msg = document.getElementById('form-msg');
  msg.textContent = 'Enviando...';
  msg.style.color = 'var(--muted)';

  const body = {
    titulo:            document.getElementById('inc-titulo').value,
    severidad:         document.getElementById('inc-severidad').value,
    tipo:              document.getElementById('inc-tipo').value || null,
    descripcion:       document.getElementById('inc-desc').value || null,
    clientesAfectados: parseInt(document.getElementById('inc-clientes').value) || 0
  };

  try {
    const res = await fetch(`${API}/incidencias`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(body)
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const created = await res.json();
    msg.textContent = `✅ Incidencia creada: ${created.codigo}`;
    msg.style.color = '#10b981';
    document.getElementById('form-incidencia').reset();
    setTimeout(() => cargarDatos(), 800); // recarga la tabla
  } catch (err) {
    msg.textContent = `❌ Error: ${err.message}. ¿Backend activo?`;
    msg.style.color = '#ef4444';
  }
}

// ── Charts (gráficos estáticos) ───────────────────────────────────────────────
Chart.defaults.color = '#8899aa';
Chart.defaults.font.family = 'Inter';

const mttrCtx = document.getElementById('mttrChart');
if (mttrCtx) {
  new Chart(mttrCtx, {
    type: 'line',
    data: {
      labels: ['Mes 1','Mes 2','Mes 3','Mes 4','Mes 5','Mes 6','Mes 7','Mes 8','Mes 9','Mes 10','Mes 11','Mes 12'],
      datasets: [{
        label: 'MTTR (horas)',
        data: [4.2, 4.0, 3.8, 3.4, 3.0, 2.6, 2.3, 2.0, 1.9, 1.8, 1.7, 1.7],
        borderColor: '#00d4ff', backgroundColor: 'rgba(0,212,255,0.1)',
        borderWidth: 2.5, pointBackgroundColor: '#00d4ff', pointRadius: 4,
        tension: 0.4, fill: true
      }]
    },
    options: {
      responsive: true,
      plugins: { legend: { display: false } },
      scales: {
        x: { grid: { color: 'rgba(255,255,255,0.05)' } },
        y: { grid: { color: 'rgba(255,255,255,0.05)' }, min: 0, max: 5,
             ticks: { callback: v => v + 'h' } }
      }
    }
  });
}

const alarmCtx = document.getElementById('alarmChart');
if (alarmCtx) {
  new Chart(alarmCtx, {
    type: 'doughnut',
    data: {
      labels: ['Alarmas suprimidas (ruido)', 'Alarmas accionables'],
      datasets: [{
        data: [85, 15],
        backgroundColor: ['rgba(124,58,237,0.7)', 'rgba(0,212,255,0.8)'],
        borderColor: ['#7c3aed', '#00d4ff'], borderWidth: 2
      }]
    },
    options: {
      responsive: true, cutout: '70%',
      plugins: {
        legend: { position: 'bottom', labels: { padding: 20 } },
        tooltip: { callbacks: { label: ctx => ` ${ctx.label}: ${ctx.parsed}%` } }
      }
    }
  });
}

// ── Animación scroll ─────────────────────────────────────────────────────────
const animObs = new IntersectionObserver(entries => {
  entries.forEach(e => {
    if (e.isIntersecting) {
      e.target.style.opacity   = '1';
      e.target.style.transform = 'translateY(0)';
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.card,.value-card,.ml-card,.tl-item,.level,.workflow-step').forEach(el => {
  el.style.opacity   = '0';
  el.style.transform = 'translateY(20px)';
  el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
  animObs.observe(el);
});

// ── Nav activo en scroll ──────────────────────────────────────────────────────
const navLinks = document.querySelectorAll('.nav-links a');
new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      navLinks.forEach(link => {
        link.style.color = link.getAttribute('href') === '#' + entry.target.id
          ? 'var(--accent)' : '';
      });
    }
  });
}, { threshold: 0.3 }).observe && document.querySelectorAll('section[id]').forEach(s =>
  new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting)
        navLinks.forEach(link => {
          link.style.color = link.getAttribute('href') === '#' + entry.target.id ? 'var(--accent)' : '';
        });
    });
  }, { threshold: 0.3 }).observe(s)
);

// ── Arrancar dashboard al cargar la página ────────────────────────────────────
window.addEventListener('DOMContentLoaded', () => {
  cargarDatos();
  // Refresca cada 30 segundos
  setInterval(cargarDatos, 30000);
});
