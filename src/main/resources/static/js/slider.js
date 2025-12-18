// 1. Konwersja danych z backendu (Java) na format slidera
// Sprawdzamy czy zmienna istnieje, jeśli nie (np. błąd ładowania) dajemy pustą tablicę
const backendData = (typeof rawSliderData !== 'undefined') ? rawSliderData : [];

// 2. Mapowanie
const products = backendData.map(item => {
    return {
        title: item.title || "",
        description: item.description,
        // ZMIANA: Teraz w DTO mamy od razu pole 'imageName', a nie zagnieżdżony obiekt
        img: '/uploads/slider/' + (item.imageName ? item.imageName : 'placeholder.jpg')
    };
});

// Reszta zmiennych bez zmian
const track = document.getElementById("track");
let cardSlots = [];
let active = 0;
let isAnimating = false;
let touchStartX = 0;
let touchStartY = 0;
let VISIBLE_CARDS;
let CENTER_INDEX;
let autoPlayTimer = null;
const AUTO_PLAY_DELAY = 3000; // Zwiększyłem trochę czas, żeby dało się czytać

function getProduct(index) {
    if (products.length === 0) return null; // Zabezpieczenie
    const n = products.length;
    const safeIndex = (index % n + n) % n;
    return products[safeIndex];
}

function renderSlot(el, product) {
    if (!product) return;

    // USUNIĘTO BUTTON Z HTML
    el.innerHTML = `
    <img src="${product.img}" class="card-img-top" alt="${product.title}">
    <div class="card-body">
      ${product.title ? `<div class="product-title">${product.title}</div>` : ''}
      <div class="product-description">${product.description}</div>
    </div>
  `;
}

// ... updateSlots(), animate() ... (BEZ ZMIAN)
function updateSlots() {
    cardSlots.forEach((el, i) => {
        const posIndex = i - CENTER_INDEX;
        el.style.setProperty('--i', posIndex);
        const product = getProduct(active + posIndex);
        renderSlot(el, product);
    });
}

function animate(direction) {
    if (isAnimating) return;
    isAnimating = true;

    if (direction === 'next') {
        active = (active + 1) % products.length;
        cardSlots.forEach(el => {
            const newPos = parseInt(el.style.getPropertyValue('--i')) - 1;
            el.style.setProperty('--i', newPos);
        });
    } else {
        active = (active - 1 + products.length) % products.length;
        cardSlots.forEach(el => {
            const newPos = parseInt(el.style.getPropertyValue('--i')) + 1;
            el.style.setProperty('--i', newPos);
        });
    }

    const elementToWatch = cardSlots[CENTER_INDEX];

    elementToWatch.addEventListener('transitionend', () => {
        let slotToReset;

        if (direction === 'next') {
            slotToReset = cardSlots.find(el => parseInt(el.style.getPropertyValue('--i')) === -CENTER_INDEX - 1);
            slotToReset.style.transition = 'none';
            slotToReset.style.setProperty('--i', CENTER_INDEX);
            renderSlot(slotToReset, getProduct(active + CENTER_INDEX));
        } else {
            slotToReset = cardSlots.find(el => parseInt(el.style.getPropertyValue('--i')) === CENTER_INDEX + 1);
            slotToReset.style.transition = 'none';
            slotToReset.style.setProperty('--i', -CENTER_INDEX);
            renderSlot(slotToReset, getProduct(active - CENTER_INDEX));
        }

        void slotToReset.offsetWidth;
        slotToReset.style.transition = '';
        isAnimating = false;
    }, { once: true });
}

// ... Touch events (BEZ ZMIAN) ...
function handleTouchStart(e) {
    touchStartX = e.touches[0].clientX;
    touchStartY = e.touches[0].clientY;
}

function handleTouchMove(e) {
    if (!touchStartX || !touchStartY) return;
    const deltaX = e.touches[0].clientX - touchStartX;
    const deltaY = e.touches[0].clientY - touchStartY;
    if (Math.abs(deltaX) > Math.abs(deltaY)) e.preventDefault();
}

function handleTouchEnd(e) {
    if (isAnimating || !touchStartX) return;
    const deltaX = e.changedTouches[0].clientX - touchStartX;
    const swipeThreshold = 50;
    if (deltaX < -swipeThreshold) {
        animate('next');
        startAutoPlay();
    } else if (deltaX > swipeThreshold) {
        animate('prev');
        startAutoPlay();
    }
    touchStartX = 0;
    touchStartY = 0;
}

function startAutoPlay() {
    if (autoPlayTimer) clearInterval(autoPlayTimer);
    autoPlayTimer = setInterval(() => animate('next'), AUTO_PLAY_DELAY);
}

function stopAutoPlay() {
    if (autoPlayTimer) clearInterval(autoPlayTimer);
}

function calculateMaxHeight() {
    if (!products || products.length === 0) return;

    // 1. Tworzymy tymczasowy, niewidoczny kontener do pomiarów
    const measureContainer = document.createElement('div');
    measureContainer.style.position = 'absolute';
    measureContainer.style.visibility = 'hidden';
    measureContainer.style.top = '-9999px';
    measureContainer.style.width = 'var(--card-width)'; // Szerokość karty z CSS
    measureContainer.style.padding = '0';

    // Musimy dodać go do body, żeby przeglądarka mogła obliczyć style
    document.body.appendChild(measureContainer);

    let maxHeight = 0;

    // 2. Iterujemy przez WSZYSTKIE produkty
    products.forEach(product => {
        // Tworzymy strukturę karty "na brudno"
        const tempCard = document.createElement('div');
        // Dodajemy style, które wpływają na wysokość (padding, fonty)
        tempCard.style.width = '100%';
        tempCard.style.display = 'flex';
        tempCard.style.flexDirection = 'column';
        tempCard.style.background = 'white'; // żeby symulować kartę

        // Renderujemy zawartość
        // UWAGA: Musi pasować do styli CSS (wysokość obrazka 260px + paddingi)
        // W stylach CSS ustawiliśmy height zdjęcia na 260px, padding body na 20px.
        // Tutaj musimy to odwzorować w prosty sposób lub wstrzyknąć HTML.

        tempCard.innerHTML = `
            <div style="height: 260px; width: 100%;"></div> <div style="padding: 20px; display: flex; flex-direction: column;">
                ${product.title ? `<div class="product-title" style="font-weight:600; font-size:1.1rem; margin-bottom:8px;">${product.title}</div>` : ''}
                <div class="product-description" style="font-size:0.9rem; margin-top:12px; margin-bottom:15px;">
                    ${product.description}
                </div>
            </div>
        `;

        measureContainer.appendChild(tempCard);

        // 3. Mierzymy wysokość
        const height = tempCard.offsetHeight;
        if (height > maxHeight) {
            maxHeight = height;
        }

        // Czyścimy kontener
        measureContainer.removeChild(tempCard);
    });

    // 4. Sprzątamy
    document.body.removeChild(measureContainer);

    // 5. Ustawiamy zmienną CSS dla całego slidera
    // Dodajemy mały zapas (np. 10px) dla bezpieczeństwa
    const finalHeight = maxHeight + 10;

    const container = document.getElementById('product-slider-container');
    if (container) {
        container.style.setProperty('--card-height', `${finalHeight}px`);
    }
}

function init() {
    const sliderContainer = document.getElementById('product-slider-container');
    if (!sliderContainer) return;

    if (products.length === 0) {
        sliderContainer.style.display = 'none';
        return;
    }

    // >>> NOWE: Obliczamy wysokość przed renderowaniem <<<
    calculateMaxHeight();

    const numProducts = products.length;
    const nextBtn = document.getElementById('nextBtn');
    const prevBtn = document.getElementById('prevBtn');
    const sliderWrap = sliderContainer.querySelector('.slider-wrap');

    if (!sliderWrap || !nextBtn || !prevBtn) return;

    // Logika trybów (1, 2, 3-4, 5+ elementów)
    if (numProducts === 1) {
        nextBtn.style.display = 'none';
        prevBtn.style.display = 'none';
        VISIBLE_CARDS = 1;
        CENTER_INDEX = 0;
        const slot = document.createElement('div');
        slot.classList.add('card-slot');
        track.appendChild(slot);
        cardSlots.push(slot);
        updateSlots();
        return;
    }

    if (numProducts === 2) {
        VISIBLE_CARDS = 3;
        sliderContainer.classList.add('slider-mode-2');
    }
    else if (numProducts >= 3 && numProducts <= 4) {
        VISIBLE_CARDS = 3;
        sliderContainer.classList.add('slider-mode-3');
    }
    else {
        VISIBLE_CARDS = 5;
        sliderContainer.classList.add('slider-mode-5');
    }

    CENTER_INDEX = Math.floor(VISIBLE_CARDS / 2);

    for (let i = 0; i < VISIBLE_CARDS; i++) {
        const slot = document.createElement('div');
        slot.classList.add('card-slot');
        track.appendChild(slot);
        cardSlots.push(slot);
    }

    updateSlots();

    nextBtn.addEventListener('click', () => {
        animate('next');
        startAutoPlay();
    });
    prevBtn.addEventListener('click', () => {
        animate('prev');
        startAutoPlay();
    });

    track.addEventListener('touchstart', handleTouchStart, { passive: false });
    track.addEventListener('touchmove', handleTouchMove, { passive: false });
    track.addEventListener('touchend', handleTouchEnd);

    sliderWrap.addEventListener('mouseenter', stopAutoPlay);
    sliderWrap.addEventListener('mouseleave', startAutoPlay);

    startAutoPlay();
}

// Uruchamiamy po załadowaniu zasobów, żeby fonty nie zmieniły wysokości tekstu
window.addEventListener('load', init);



