/* Sai da Bet — PWA (v0.1.0 web). Tudo fica no localStorage deste aparelho. */
(() => {
  const KEY = 'saidabet:v1';
  const TARGETS = [
    ['bet365','Bet365',['bet365.com','bet365.bet.br']],
    ['blaze','Blaze',['blaze.com','blaze.bet.br']],
    ['betano','Betano',['betano.bet.br','betano.com']],
    ['sportingbet','Sportingbet',['sportingbet.bet.br','sportingbet.com']],
    ['pixbet','Pixbet',['pixbet.bet.br','pixbet.com']],
    ['stake','Stake',['stake.com','stake.bet.br']],
    ['esportesdasorte','Esportes da Sorte',['esportesdasorte.bet.br','esportesdasorte.com']],
    ['betfair','Betfair',['betfair.bet.br','betfair.com']],
    ['rivalo','Rivalo',['rivalo.bet.br','rivalo.com']],
    ['novibet','Novibet',['novibet.bet.br','novibet.com']],
    ['mrjack','MrJack',['mrjack.bet.br','mrjack.bet']],
    ['superbet','Superbet',['superbet.bet.br','superbet.com']],
    ['betnacional','Betnacional',['betnacional.bet.br']]
  ];
  const QUOTES = [
    'Recuperar o que perdeu não é apostar de novo. Recuperar é parar.',
    'A vontade é uma onda: sobe, chega no pico e passa.',
    'A casa sempre ganha no longo prazo. Hoje, quem ganha é você.',
    'Um dia de cada vez. Só precisa vencer o dia de hoje.',
    'Você não é fraco. As bets são feitas pra viciar.',
    'Cada real que fica no bolso é uma vitória.'
  ];
  const GOALS = [1,3,7,14,30,60,90,180,365];
  const $ = s => document.querySelector(s);
  const $$ = s => [...document.querySelectorAll(s)];
  const today = () => { const d = new Date(); d.setMinutes(d.getMinutes() - d.getTimezoneOffset()); return d.toISOString().slice(0,10); };
  const brl = n => n.toLocaleString('pt-BR',{style:'currency',currency:'BRL',maximumFractionDigits:0});

  let st = load();
  function load(){ try { return JSON.parse(localStorage.getItem(KEY)) || {}; } catch { return {}; } }
  function save(){ localStorage.setItem(KEY, JSON.stringify(st)); }
  function toast(msg){ const t=$('#toast'); t.textContent=msg; t.hidden=false; clearTimeout(toast.h); toast.h=setTimeout(()=>t.hidden=true,2200); }
  function startDate(){ return new Date((st.start || today()) + 'T00:00:00'); }

  // ---------- navegação ----------
  function go(id){
    $$('.screen').forEach(s => s.hidden = s.id !== id);
    $('#tabbar').hidden = !st.onboarded || id === 'settings';
    $$('#tabbar button').forEach(b => b.classList.toggle('on', b.dataset.go === id));
    if (id === 'home') renderHome();
    if (id === 'block') renderBlock();
    if (id === 'sos') renderSos();
    if (id === 'settings') { $('#set-date').value = st.start || today(); $('#set-spend').value = st.spend || ''; }
    window.scrollTo(0,0);
    if (st.onboarded) history.replaceState(null,'','#'+id);
  }
  document.addEventListener('click', e => { const b = e.target.closest('[data-go]'); if (b) go(b.dataset.go); });

  // ---------- onboarding ----------
  let step = 0;
  function showStep(n){ step=n; $$('.ob-step').forEach(s => s.hidden = +s.dataset.step !== n); $$('.dots i').forEach((d,i)=>d.classList.toggle('on', i===n)); }
  $$('[data-next]').forEach(b => b.addEventListener('click', () => {
    if (step === 1) {
      const d = $('#ob-date').value || today();
      if (d > today()) return toast('A data não pode ser no futuro');
      st.start = d; st.spend = Math.max(0, parseFloat($('#ob-spend').value) || 0);
    }
    showStep(step+1);
  }));
  $('#ob-finish').addEventListener('click', () => {
    st.supportName = $('#ob-name').value.trim(); st.supportPhone = $('#ob-phone').value.trim();
    st.onboarded = true; st.blocked = st.blocked || Object.fromEntries(TARGETS.map(t => [t[0], true]));
    save(); go('home'); maybeIosTip();
  });

  // ---------- home ----------
  let tick;
  function renderHome(){
    clearInterval(tick);
    const upd = () => {
      const ms = Math.max(0, Date.now() - startDate().getTime());
      const days = Math.floor(ms / 864e5);
      const rem = ms % 864e5, h = Math.floor(rem/36e5), m = Math.floor(rem%36e5/6e4), s = Math.floor(rem%6e4/1e3);
      $('#days').textContent = days;
      $('#clock').textContent = `${String(h).padStart(2,'0')}h ${String(m).padStart(2,'0')}m ${String(s).padStart(2,'0')}s`;
      $('#saved').textContent = brl((st.spend||0) * (ms/864e5));
      const next = GOALS.find(g => g > days) || (Math.ceil((days+1)/365)*365);
      const prev = [...GOALS].reverse().find(g => g <= days) || 0;
      $('#goal').textContent = next + (next === 1 ? ' dia' : ' dias');
      $('#goalbar').style.width = Math.min(100, ((ms/864e5 - prev) / (next - prev)) * 100) + '%';
    };
    $('#since').textContent = 'desde ' + startDate().toLocaleDateString('pt-BR',{day:'2-digit',month:'long',year:'numeric'});
    $('#quote').textContent = '“' + QUOTES[new Date().getDate() % QUOTES.length] + '”';
    upd(); tick = setInterval(upd, 1000);
  }
  $('#relapse').addEventListener('click', () => {
    if (!confirm('Tudo bem recair — o importante é recomeçar. Zerar o contador a partir de hoje?')) return;
    st.start = today(); st.relapses = (st.relapses||0) + 1; save(); renderHome(); toast('Contador reiniciado. Um dia de cada vez 💚');
  });

  // ---------- bloqueios ----------
  function renderBlock(){
    st.blocked = st.blocked || {};
    $('#blocklist').innerHTML = TARGETS.map(([id,name,doms]) => `
      <li><div>${name}<small>${doms.join(' · ')}</small></div>
      <label class="sw"><input type="checkbox" data-id="${id}" ${st.blocked[id] !== false ? 'checked':''}><span></span></label></li>`).join('');
  }
  $('#blocklist').addEventListener('change', e => { const id = e.target.dataset.id; if (!id) return; st.blocked[id] = e.target.checked; save(); });
  $('#copy-sites').addEventListener('click', async () => {
    const list = TARGETS.filter(t => (st.blocked||{})[t[0]] !== false).flatMap(t => t[2]).join('\n');
    try { await navigator.clipboard.writeText(list); toast('Sites copiados! Cole em Ajustes › Tempo de Uso'); }
    catch { prompt('Copie os sites:', list.replace(/\n/g, ', ')); }
  });

  // ---------- SOS ----------
  function waLink(){
    let d = (st.supportPhone||'').replace(/\D/g,''); if (d.length < 10) return null;
    if (!d.startsWith('55')) d = '55' + d;
    return `https://wa.me/${d}?text=${encodeURIComponent('Oi, tô numa hora difícil com vontade de apostar. Pode me ajudar a distrair um pouco?')}`;
  }
  function renderSos(){
    const link = waLink(), btn = $('#wa-btn');
    $('#sos-name').value = st.supportName||''; $('#sos-phone').value = st.supportPhone||'';
    if (link) { btn.href = link; btn.classList.remove('ghost'); btn.textContent = '💬 WhatsApp pra ' + (st.supportName || 'meu contato'); $('#wa-hint').textContent = ''; }
    else { btn.removeAttribute('href'); btn.classList.add('ghost'); btn.textContent = '💬 Cadastrar contato de apoio'; $('#wa-hint').textContent = 'Cadastre alguém de confiança logo abaixo.'; }
  }
  $('#wa-btn').addEventListener('click', e => { if (!waLink()) { e.preventDefault(); document.querySelector('details').open = true; $('#sos-name').focus(); } });
  $('#sos-save').addEventListener('click', () => { st.supportName = $('#sos-name').value.trim(); st.supportPhone = $('#sos-phone').value.trim(); save(); renderSos(); toast('Contato salvo'); });
  let breathing;
  $('#circle').addEventListener('click', () => {
    if (breathing) return;
    const c = $('#circle'), txt = $('#breathe-txt'), tm = $('#breathe-timer'); let left = 90, phase = 0;
    const cycle = () => { phase = 1 - phase; c.className = 'circle ' + (phase ? 'in' : 'out'); txt.textContent = phase ? 'Inspire…' : 'Solte devagar…'; };
    cycle(); const cyc = setInterval(cycle, 4000);
    breathing = setInterval(() => { left--; tm.textContent = left + 's — a vontade está passando';
      if (left <= 0) { clearInterval(breathing); clearInterval(cyc); breathing = null; c.className = 'circle'; txt.textContent = 'Você conseguiu 💚'; tm.textContent = 'Toque pra fazer de novo'; } }, 1000);
  });

  // ---------- ajustes ----------
  $('#set-save').addEventListener('click', () => {
    const d = $('#set-date').value || today(); if (d > today()) return toast('A data não pode ser no futuro');
    st.start = d; st.spend = Math.max(0, parseFloat($('#set-spend').value) || 0); save(); toast('Salvo'); go('home');
  });
  $('#wipe').addEventListener('click', () => { if (confirm('Apagar todos os dados deste aparelho?')) { localStorage.removeItem(KEY); location.hash=''; location.reload(); } });

  // ---------- instalar (iOS) ----------
  const isIos = /iphone|ipad|ipod/i.test(navigator.userAgent);
  const standalone = window.matchMedia('(display-mode: standalone)').matches || navigator.standalone;
  function maybeIosTip(){ if (isIos && !standalone && !st.tipClosed) $('#ios-tip').hidden = false; }
  $('#ios-tip-x').addEventListener('click', () => { $('#ios-tip').hidden = true; st.tipClosed = true; save(); });
  if (standalone) $('#install-card').hidden = true;

  // ---------- boot ----------
  $('#ob-date').value = today(); $('#ob-date').max = today(); $('#set-date').max = today();
  if (!st.onboarded) { $('#onboarding').hidden = false; showStep(0); }
  else { const h = location.hash.slice(1); go(['home','block','sos','plan','settings'].includes(h) ? h : 'home'); maybeIosTip(); }
  if ('serviceWorker' in navigator) window.addEventListener('load', () => navigator.serviceWorker.register('./sw.js', { scope: './' }).catch(()=>{}));
})();
