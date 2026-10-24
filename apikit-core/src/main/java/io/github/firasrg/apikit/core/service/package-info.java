/**
 * Capability-oriented service contracts.
 *
 * <p>Small interfaces ({@code ReadService}, {@code CreateService}, {@code UpdateService}, {@code
 * DeleteService}, {@code SearchService}) can be implemented independently or composed (e.g. {@code
 * CrudService}), so read-only and partial-CRUD resources stay natural to implement.
 *
 * <p>Contracts are framework-independent. Transaction semantics are defined by the implementation,
 * not by these interfaces.
 */
package io.github.firasrg.apikit.core.service;
