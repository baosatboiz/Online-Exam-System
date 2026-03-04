import { useEffect, useRef } from 'react';
import './LoadingPage.css';

/* ── Particle config ── */
const PARTICLE_COUNT = 20;

function generateParticles() {
  return Array.from({ length: PARTICLE_COUNT }, (_, i) => {
    const size = Math.random() * 3 + 1.5;
    return {
      id: i,
      size,
      left: `${Math.random() * 100}%`,
      top: `${30 + Math.random() * 65}%`,
      duration: `${7 + Math.random() * 9}s`,
      delay: `${Math.random() * 9}s`,
      color: Math.random() > 0.5 ? '#1db87e' : '#5ab4f0',
    };
  });
}

/* ── Logo SVG (shield + book + cap + brand text) ── */
function LogoSVG() {
  return (
    <svg
      className="logo-static"
      viewBox="0 0 200 215"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <defs>
        <linearGradient id="sG" x1="50" y1="10" x2="150" y2="170" gradientUnits="userSpaceOnUse">
          <stop offset="0%"   stopColor="#2048b8" />
          <stop offset="100%" stopColor="#0d1e5a" />
        </linearGradient>
        <linearGradient id="bG" x1="60" y1="80" x2="140" y2="140" gradientUnits="userSpaceOnUse">
          <stop offset="0%"   stopColor="#c8ddf8" />
          <stop offset="100%" stopColor="#9ab8e8" />
        </linearGradient>
        <linearGradient id="cG" x1="75" y1="85" x2="125" y2="135" gradientUnits="userSpaceOnUse">
          <stop offset="0%"   stopColor="#ffffff" />
          <stop offset="100%" stopColor="#c8ddf8" />
        </linearGradient>
        <linearGradient id="kG" x1="60" y1="20" x2="140" y2="55" gradientUnits="userSpaceOnUse">
          <stop offset="0%"   stopColor="#1a3a8c" />
          <stop offset="100%" stopColor="#0a1840" />
        </linearGradient>
        <filter id="g2">
          <feGaussianBlur stdDeviation="2" result="b" />
          <feMerge>
            <feMergeNode in="b" />
            <feMergeNode in="SourceGraphic" />
          </feMerge>
        </filter>
      </defs>

      {/* Shield outer */}
      <path
        d="M100 14 L164 42 L164 110 Q164 156 100 180 Q36 156 36 110 L36 42 Z"
        fill="url(#sG)" stroke="#2a5bd4" strokeWidth="2"
      />
      {/* Shield inner border */}
      <path
        d="M100 26 L152 50 L152 108 Q152 144 100 164 Q48 144 48 108 L48 50 Z"
        fill="none" stroke="rgba(90,150,240,.5)" strokeWidth="1.8"
      />

      {/* Circuit left */}
      <line x1="54" y1="78" x2="66" y2="78" stroke="#4a90d9" strokeWidth="1.5" strokeLinecap="round" />
      <circle cx="54" cy="78" r="2.5" fill="#4a90d9" />
      <line x1="54" y1="78" x2="54" y2="94" stroke="#4a90d9" strokeWidth="1.5" />
      <circle cx="54" cy="94" r="2" fill="#4a90d9" opacity=".6" />

      {/* Circuit right */}
      <line x1="146" y1="78" x2="134" y2="78" stroke="#4a90d9" strokeWidth="1.5" strokeLinecap="round" />
      <circle cx="146" cy="78" r="2.5" fill="#4a90d9" />
      <line x1="146" y1="78" x2="146" y2="94" stroke="#4a90d9" strokeWidth="1.5" />
      <circle cx="146" cy="94" r="2" fill="#4a90d9" opacity=".6" />

      {/* Book */}
      <rect x="98" y="86" width="4" height="52" rx="1" fill="#8ab0d8" />
      <path d="M100 88 Q80 84 62 90 L62 136 Q80 130 100 133 Z"      fill="url(#bG)" stroke="#7aa0cc" strokeWidth="1" />
      <path d="M100 88 Q120 84 138 90 L138 136 Q120 130 100 133 Z"  fill="url(#bG)" stroke="#7aa0cc" strokeWidth="1" />
      <path d="M62 136 Q81 145 100 142 Q119 145 138 136"            fill="none"     stroke="#7aa0cc" strokeWidth="1.5" />
      <line x1="68" y1="100" x2="95"  y2="98"  stroke="rgba(50,80,160,.3)" strokeWidth="1" />
      <line x1="68" y1="107" x2="95"  y2="105" stroke="rgba(50,80,160,.3)" strokeWidth="1" />
      <line x1="68" y1="114" x2="95"  y2="112" stroke="rgba(50,80,160,.3)" strokeWidth="1" />
      <line x1="105" y1="98"  x2="132" y2="100" stroke="rgba(50,80,160,.3)" strokeWidth="1" />
      <line x1="105" y1="105" x2="132" y2="107" stroke="rgba(50,80,160,.3)" strokeWidth="1" />
      <line x1="105" y1="112" x2="132" y2="114" stroke="rgba(50,80,160,.3)" strokeWidth="1" />

      {/* Checkmark */}
      <path
        d="M74 110 L89 126 L126 94"
        stroke="url(#cG)" strokeWidth="8"
        strokeLinecap="round" strokeLinejoin="round"
        fill="none" filter="url(#g2)"
      />

      {/* Graduation cap */}
      <path d="M65 52 L100 40 L135 52 L100 64 Z" fill="url(#kG)" stroke="rgba(90,150,240,.4)" strokeWidth="1.2" />
      <path d="M82 54 L82 70 Q82 78 100 80 Q118 78 118 70 L118 54" fill="url(#kG)" stroke="rgba(90,150,240,.4)" strokeWidth="1.2" />
      <path d="M65 52 L100 40 L135 52 L100 64 Z" fill="none" stroke="rgba(160,200,255,.2)" strokeWidth=".8" />
      <line x1="135" y1="52" x2="135" y2="68" stroke="#4a90d9" strokeWidth="2" strokeLinecap="round" />
      <circle cx="135" cy="71" r="4" fill="#4a90d9" />

      {/* Brand text */}
      <text
        x="100" y="202"
        textAnchor="middle"
        fontFamily="'Exo 2', sans-serif"
        fontWeight="900" fontSize="20"
        fill="#ffffff" letterSpacing="1.5"
      >
        ONLINE EXAM
      </text>
      {/* Accent dot — O */}
      <circle cx="22"  cy="195.5" r="3.5" fill="#1db87e" filter="url(#g2)" />
      {/* Accent dot — A in EXAM */}
      <circle cx="180" cy="195.5" r="3.5" fill="#1db87e" filter="url(#g2)" />
      <text
        x="100" y="213"
        textAnchor="middle"
        fontFamily="'Exo 2', sans-serif"
        fontWeight="600" fontSize="11"
        fill="#5a7ab0" letterSpacing="6"
      >
        SYSTEM
      </text>
    </svg>
  );
}

/* ── Primary spinner ring (comet arc) ── */
function SpinnerRing() {
  return (
    <svg
      className="spinner-ring"
      viewBox="0 0 238 238"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <defs>
        <linearGradient id="cometGrad" x1="0" y1="0" x2="0" y2="238" gradientUnits="userSpaceOnUse">
          <stop offset="0%"   stopColor="#13f0a0" stopOpacity="0" />
          <stop offset="55%"  stopColor="#1db87e" stopOpacity=".8" />
          <stop offset="85%"  stopColor="#13f0a0" />
          <stop offset="100%" stopColor="#ffffff" />
        </linearGradient>
        <filter id="cometGlow">
          <feGaussianBlur stdDeviation="3.5" result="blur" />
          <feMerge>
            <feMergeNode in="blur" />
            <feMergeNode in="SourceGraphic" />
          </feMerge>
        </filter>
        <filter id="headGlow">
          <feGaussianBlur stdDeviation="5" result="blur" />
          <feMerge>
            <feMergeNode in="blur" />
            <feMergeNode in="SourceGraphic" />
          </feMerge>
        </filter>
      </defs>

      {/* Faint full track ring */}
      <circle
        cx="119" cy="119" r="112"
        stroke="rgba(29,184,126,.22)"
        strokeWidth="2"
        strokeDasharray="5 7"
      />

      {/* Comet arc — circumference ≈ 703.7, 280° arc ≈ 548, gap ≈ 156 */}
      <circle
        cx="119" cy="119" r="112"
        stroke="url(#cometGrad)"
        strokeWidth="3.5"
        strokeLinecap="round"
        strokeDasharray="548 156"
        filter="url(#cometGlow)"
      />

      {/* Bright head dot at top (119, 7) */}
      <circle cx="119" cy="7" r="6"  fill="#ffffff" filter="url(#headGlow)" />
      <circle cx="119" cy="7" r="10" fill="none"    stroke="rgba(255,255,255,.25)" strokeWidth="1.5" />

      {/* Circuit node dots */}
      <circle cx="231" cy="119" r="4.5" fill="#1db87e" filter="url(#cometGlow)" opacity=".8" />
      <circle cx="7"   cy="119" r="4.5" fill="#13f0a0" filter="url(#cometGlow)" opacity=".8" />
      <circle cx="119" cy="231" r="4.5" fill="#5ab4f0" filter="url(#cometGlow)" opacity=".8" />
    </svg>
  );
}

/* ── Secondary counter-spin dashed ring ── */
function SpinnerRing2() {
  return (
    <svg
      className="spinner-ring2"
      viewBox="0 0 212 212"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <circle
        cx="106" cy="106" r="102"
        stroke="rgba(90,180,240,.45)"
        strokeWidth="1.5"
        strokeDasharray="9 11"
      />
    </svg>
  );
}

/* ══════════════════════════════════════
   Main LoadingPage component
══════════════════════════════════════ */
export default function LoadingPage() {
  const particlesRef = useRef(null);

  // Generate particles once on mount
  useEffect(() => {
    const container = particlesRef.current;
    if (!container) return;

    const particles = generateParticles();
    particles.forEach(({ id, size, left, top, duration, delay, color }) => {
      const el = document.createElement('div');
      el.className = 'particle';
      el.style.cssText = `
        width: ${size}px;
        height: ${size}px;
        left: ${left};
        top: ${top};
        --d: ${duration};
        animation-delay: ${delay};
        background: ${color};
      `;
      container.appendChild(el);
    });

    return () => {
      // cleanup on unmount
      while (container.firstChild) container.removeChild(container.firstChild);
    };
  }, []);

  return (
    <div className="loading-page">
      {/* Background layers */}
      <div className="bg-mesh" />
      <div className="bg-grid" />
      <div className="particles" ref={particlesRef} />

      {/* Main content card */}
      <div className="loadingcard">
        <div className="logo-wrap">
          <SpinnerRing />
          <SpinnerRing2 />
          <LogoSVG />
        </div>

        {/* Bouncing dots */}
        <div className="loading-dots">
          <div className="dot" />
          <div className="dot" />
          <div className="dot" />
        </div>
        <p className="loading-label">Đang tải hệ thống…</p>
      </div>

      {/* Bottom accent bar */}
      <div className="bottom-bar" />
      <span className="version-tag">v0.0.1 · SECURE SESSION</span>
    </div>
  );
}